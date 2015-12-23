package com.pemass.service.pemass.biz.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pemass.common.core.exception.BaseBusinessException;
import com.pemass.common.core.pojo.Body;
import com.pemass.common.core.pojo.DomainPage;
import com.pemass.common.core.util.ArithmeticUtil;
import com.pemass.common.server.dao.BaseDao;
import com.pemass.common.server.domain.Expression;
import com.pemass.common.server.domain.Operation;
import com.pemass.common.server.enumeration.AvailableEnum;
import com.pemass.common.server.util.MergeUtil;
import com.pemass.persist.dao.poi.PresentPackDao;
import com.pemass.persist.dao.biz.ProductDao;
import com.pemass.persist.domain.jpa.bas.Province;
import com.pemass.persist.domain.jpa.biz.AccountRelation;
import com.pemass.persist.domain.jpa.biz.Product;
import com.pemass.persist.domain.jpa.biz.ProductCategory;
import com.pemass.persist.domain.jpa.biz.ProductImage;
import com.pemass.persist.domain.jpa.sys.Organization;
import com.pemass.persist.domain.redis.Config;
import com.pemass.persist.enumeration.AuditStatusEnum;
import com.pemass.persist.enumeration.ConfigEnum;
import com.pemass.persist.enumeration.SequenceEnum;
import com.pemass.service.exception.BizError;
import com.pemass.service.pemass.bas.ProvinceService;
import com.pemass.service.pemass.bas.SequenceService;
import com.pemass.service.pemass.biz.AccountRelationService;
import com.pemass.service.pemass.biz.ProductCategoryService;
import com.pemass.service.pemass.biz.ProductService;
import com.pemass.service.pemass.sys.ConfigService;
import com.pemass.service.pemass.sys.OrganizationService;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @Description: ProductServiceImpl
 * @Author: luoc
 * @CreateTime: 2014-10-13 10:50
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private BaseDao jpaBaseDao;

    @Resource
    private PresentPackDao presentPackDao;

    @Resource
    private ProductDao productDao;

    @Resource
    private ObjectMapper jacksonObjectMapper;

    @Resource
    private ProductCategoryService productCategoryService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private ProvinceService provinceService;

    @Resource
    private SequenceService sequenceService;

    @Resource
    private AccountRelationService accountRelationService;

    @Resource
    private ConfigService configService;

    //==========================
    @Override
    public void saveProduct(Product product) {
        Preconditions.checkNotNull(product);
        productDao.persist(product);
    }


    @Override
    public Product selectProductById(Long productId) {
        Preconditions.checkNotNull(productId);
        return productDao.getEntityById(Product.class, productId);
    }

    @Override
    public Product updateProduct(Product source) {
        Preconditions.checkNotNull(source);

        Product targetProduct = selectProductById(source.getId());
        MergeUtil.merge(source, targetProduct);
        targetProduct.setUpdateTime(DateTime.now().toDate());
        return productDao.merge(targetProduct);
    }

    /**
     * 多条件查询商品
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param compoundFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public DomainPage getAllproducts(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        DomainPage domainPage = productDao.getProductsByCompoundConditions(Product.class, fieldNameValueMap, fuzzyFieldNameValueMap, compoundFieldNameValueMap, pageIndex, pageSize);
        if (domainPage != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[4];
                objects[0] = domainPage.getDomains().get(i);
                if (((Product) domainPage.getDomains().get(i)).getParentProductId() != 0) {
                    Product product = productDao.getEntityById(Product.class, ((Product) domainPage.getDomains().get(i)).getOriginProductId());
                    objects[1] = product;
                } else {
                    objects[1] = domainPage.getDomains().get(i);
                }
                objects[2] = organizationService.getOrganizationById(((Product) objects[0]).getOrganizationId());
                Organization organization = organizationService.getOrganizationById(((Product) objects[0]).getOrganizationId());
                objects[3] = provinceService.getProvinceByID(organization.getProvinceId());
                domainPage.getDomains().set(i, objects);
            }
        }
        return domainPage;
    }

    @Override
    public DomainPage getAllproducts(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap,
                                     Map<String, Object> compoundFieldNameValueMap, String fieldName,
                                     String sortIn, long pageIndex, long pageSize) {
        return productDao.getProductsByCompoundConditions(Product.class, fieldNameValueMap, fuzzyFieldNameValueMap, compoundFieldNameValueMap, fieldName, sortIn, pageIndex, pageSize);
    }

    /**
     * 删除商品
     *
     * @param id
     * @return
     */
    @Override
    public void deleteProduct(Long id) {
        List<Product> products = productDao.getRelatedProducts(id);
        for (Product product : products) {
            product.setAvailable(AvailableEnum.UNAVAILABLE);
            jpaBaseDao.merge(product);
        }
    }

    /**
     * 下架商品
     *
     * @param id
     * @return
     */
    @Override
    public void updatedDisable(Long id) {
        List<Product> products = productDao.getRelatedProducts(id);
        for (Product product : products) {
            product.setProductStatus(1);
            jpaBaseDao.merge(product);
        }
    }

    /**
     * 验证商品是否能上架
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean isShelves(String ids) {
        String myId[] = ids.split(",");
        int flag = 0;
        for (int i = 0; i < myId.length; i++) {
            Long id = Long.parseLong(myId[i]);
            Product product = productDao.getEntityById(Product.class, id);
            if (product.getParentProductId() != 0) {
                Product parentProduct = productDao.getEntityById(Product.class, product.getParentProductId());
                if (parentProduct.getProductStatus() == 1) {
                    flag++;
                }
            }
        }
        if (flag == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 上架商品
     *
     * @param id
     * @return
     */
    @Override
    public Boolean updateShelves(Long id) {
        Product product = jpaBaseDao.getEntityById(Product.class, id);
        product.setProductStatus(2);
        jpaBaseDao.merge(product);
        if (product.getProductStatus() != 2) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取商品信息
     *
     * @param id
     * @return
     */
    @Override
    public Product getProductInfo(Long id) {
        Product product = jpaBaseDao.getEntityById(Product.class, id);
        return product;
    }

    @Override
    public Product updateStandardCommodity(Product product) {
        return updateProduct(product);
    }

    /**
     * 查询所有已上架商品集合
     *
     * @param map
     * @return
     */
    @Override
    public List<Product> getProductList(Map<String, Object> map) {
        return productDao.getShelvesOfProducts(map);
    }

    /**
     * 商户新增商品
     *
     * @param product
     * @return
     */
    @Override
    public Product addProduct(Product product, String isOneProduct) {
        ProductCategory productCategory = productCategoryService.getProductCategoryById(product.getProductCategoryId());
        if (productCategory.getIsCreateTicket() == 0) {
            product.setIsTimePeriod(-1);
            product.setShareContent(null);
        }
        Organization organization = organizationService.getOrganizationById(product.getOrganizationId());
//        if (organization.getIsOneMerchant() == 0) {
        product.setIsCashback(0);
//        }
        //申请一元购商品
        if (isOneProduct != null && isOneProduct.equals("1")) {
            Config config = configService.getConfigByKey(ConfigEnum.LOAN_RATE);
            product.setOneProductApplyStatus(AuditStatusEnum.ING_AUDIT);
            product.setOneProductStatus(1);
            product.setOnePrice(1D);
            product.setLoanPrice(ArithmeticUtil.floor(ArithmeticUtil.sub(product.getOutPrice(), 1),2));
        } else {
            product.setOneProductApplyStatus(AuditStatusEnum.NONE_AUDIT);
            product.setOneProductStatus(0);
            product.setLoanPrice(0D);
        }
        if (product.getIsPresentPoint() == 0) {
            product.setGivingPnumber(0);
            product.setGivingEnumber(0);
        }
        product.setParentProductId(0L);
        product.setOriginProductId(0L);
        if (product.getCashbackRatio() == null) {
            product.setCashbackRatio(0D);
        } else {
            product.setCashbackRatio(ArithmeticUtil.floor(ArithmeticUtil.div(product.getCashbackRatio(),100),3));
        }
        //设置商品编号
        product.setProductIdentifier(sequenceService.obtainSequence(SequenceEnum.PRODUCT));

        product.setPath("");
        product.setSaleNumber(0);
        jpaBaseDao.persist(product);
        product.setOriginProductId(product.getId());
        product.setPath("/" + product.getId() + "/");
        //更新商品信息
        jpaBaseDao.merge(product);
        return product;
    }

    /**
     * 查询上级可以分销的商品
     *
     * @param fieldNameValueMap
     * @param fuzzyFieldNameValueMap
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public DomainPage getParentProductList(Long orgId, Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {

        DomainPage domainPage = productDao.getEntitiesPagesByFieldList(Product.class, orgId, fieldNameValueMap, fuzzyFieldNameValueMap, compoundFieldNameValueMap, pageIndex, pageSize);
        if (domainPage != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domainPage.getDomains().get(i);
                if (((Product) domainPage.getDomains().get(i)).getParentProductId() != 0) {
                    Product product = productDao.getEntityById(Product.class, ((Product) domainPage.getDomains().get(i)).getOriginProductId());
                    objects[1] = product;
                }
                domainPage.getDomains().set(i, objects);
            }
        }
        return domainPage;
    }

    /**
     * body排序
     *
     * @param title
     * @param imageUrl
     * @param content
     * @return
     */
    @Override
    public List<Body> getProductDetailBodies(List<String> title, List<String> imageUrl, List<String> content) {
        if (title == null) {
            title = new ArrayList<String>();
        }
        if (imageUrl == null) {
            imageUrl = new ArrayList<String>();
        }
        if (content == null) {
            content = new ArrayList<String>();
        }
        List<Body> bodies = new ArrayList<Body>();
        if (imageUrl != null && content != null && title != null) {
            int[] compare = new int[]{imageUrl.size(), content.size(), title.size()};
            Arrays.sort(compare);//升序排列数组
            Object[] bodyArray = new Object[compare[compare.length - 1]];//初始化数组长度为三个list的最大size
            for (int i = 0; i < title.size(); i++) {
                if (title.get(i) != null || title.get(i) == "") {
                    Body body = new Body();
                    body.setTitle("");
                    body.setType(Body.BodyType.TITLE);
                    body.setValue(title.get(i));
                    bodyArray[i] = body;
                }
            }
            //根据imageUrl下标放进数组中对应的位置
            for (int i = 0; i < imageUrl.size(); i++) {
                if (imageUrl.get(i) != null || imageUrl.get(i) == "") {
                    Body body = new Body();
                    body.setTitle("");
                    body.setType(Body.BodyType.IMAGE);
                    body.setValue(imageUrl.get(i));
                    bodyArray[i] = body;
                }
            }
            //根据content下标放进数组中对应的位置
            for (int i = 0; i < content.size(); i++) {
                if (content.get(i) != null || content.get(i) == "") {
                    Body body = new Body();
                    body.setTitle("");
                    body.setType(Body.BodyType.TXT);
                    body.setValue(content.get(i));
                    bodyArray[i] = body;
                }
            }
            //把数组中的值按顺序放入List
            for (int i = 0; i < bodyArray.length; i++) {
                if (bodyArray[i] != null) {
                    bodies.add((Body) bodyArray[i]);
                }
            }
        }
        return bodies;
    }

    /**
     * 封装Body(审核后台 - 修改商品信息<景区标准门票>)
     *
     * @param bodies
     * @return
     */
    @Override
    public String getBodies(List<Body> bodies) {
        String productDetail = null;
        try {
            productDetail = jacksonObjectMapper.writeValueAsString(bodies);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productDetail;
    }

    /**
     * 对可分销的商品操作：加入商品仓库/直接上架
     *
     * @param product
     * @return
     */
    @Transactional
    @Override
    public Product addProductDepot(Product product, Organization organization, Integer productStatus, List<ProductImage> productImageList) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("organizationId", organization.getId());
        map.put("parentOrganizationId", product.getOrganizationId());
        map.put("auditStatus", AuditStatusEnum.HAS_AUDIT);
        map.put("discountStatus", AuditStatusEnum.HAS_AUDIT);
        List<Object[]> list = accountRelationService.getAllAssociated(map);
        if (list != null && list.size() > 0) {
            Object[] object = list.get(0);
            AccountRelation accountRelation = (AccountRelation) object[0];
            product.setBasePrice(ArithmeticUtil.floor((Float.valueOf(accountRelation.getDiscount()) / 10) * product.getLowerPrice(), 2));
        }
        product.setLowerPrice(0D);
        product.setOrganizationId(organization.getId());
        product.setProductStatus(productStatus);
        product.setParentProductId(product.getId());
        product.setOriginProductId(product.getOriginProductId());
        if (organization.getIsOneMerchant() != 1) {
            product.setIsCashback(0);
            product.setCashbackRatio(0D);
        }
        product.setOneProductStatus(0);
        product.setOneProductApplyStatus(AuditStatusEnum.NONE_AUDIT);
        product.setOnePrice(0D);
        product.setLoanPrice(0D);
        product.setId(null);
        jpaBaseDao.persist(product);
        product.setPath(product.getPath() + product.getId() + "/");
        jpaBaseDao.merge(product);
        if (productImageList != null && productImageList.size() > 0) {
            for (ProductImage productImage : productImageList) {
                productImage.setProductId(product.getId());
                productImage.setId(null);
                insertImage(productImage);
            }
        }
        return product;
    }

    /**
     * 查询分销商分销的某供应商的商品
     *
     * @param parentAccountId
     * @param accountId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public DomainPage getDistributionProduct(Long parentAccountId, Long accountId, Long pageIndex, Long pageSize) {
        return productDao.getDistributionProduct(Product.class, parentAccountId, accountId, pageIndex, pageSize);
    }

    @Override
    public List<Product> getAllProducts(Map<String, Object> map) {
        return jpaBaseDao.getEntitiesByFieldList(Product.class, map);
    }

    @Override
    public DomainPage getProductsByConditions(Map<String, Object> conditions, Map<String, Object> fuzzyConditions, Map<String, List<Object>> collectionConditions, DomainPage domainPage) {
        return presentPackDao.getEntitiesPagesByFieldList(Product.class, conditions, fuzzyConditions, collectionConditions, null, null, domainPage.getPageIndex(), domainPage.getPageSize());
    }

    @Override
    public DomainPage getProductsByConditions(Map<String, Object> conditions, DomainPage domainPage) {
        return productDao.getVirtualProductByConditions(conditions, domainPage);
    }

    @Override
    public DomainPage getProductDistributionDetail(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, long pageIndex, long pageSize) {
        DomainPage domainPage = productDao.getProductDistributionDetail(Product.class, fieldNameValueMap, fuzzyFieldNameValueMap, pageIndex, pageSize);
        List<Object[]> list = Lists.newArrayList();
        if (domainPage.getDomains() != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[2];
                objects[0] = domainPage.getDomains().get(i);
                long oid = new BigInteger(((Product) objects[0]).getOrganizationId().toString()).longValue();
                objects[1] = organizationService.getOrganizationById(oid);
                list.add(objects);
            }
        }
        domainPage.setDomains(list);
        return domainPage;
    }

    @Override
    public void insertImage(ProductImage productImage) {
        jpaBaseDao.persist(productImage);
    }

    @Override
    public boolean checkProductUsableAmount(Long productId, Integer needAmount) {
        Product product = jpaBaseDao.getEntityById(Product.class, productId);
        boolean checkStatus = false;
        if (product != null) {
            if (product.getStockNumber() != -1) {
                if (product.getStockNumber() >= needAmount) {
                    checkStatus = true;
                }
            } else {
                checkStatus = true;
            }
        }
        return checkStatus;
    }

    @Override
    public void updateNonStandardProduct(Product product, String isOneProduct) {
        ProductCategory productCategory = null;
        if (product.getParentProductId() == null) {
            productCategory = productCategoryService.getProductCategoryById(product.getProductCategoryId());
        }

        List<Product> products = productDao.getRelatedProducts(product.getId());
        long id = product.getId();
        product.setId(null);
        if (products != null && products.size() > 0) {
            for (Product pd : products) {
                if (pd.getId() == id) {//如果是当前商品则修改全部内容
                    MergeUtil.merge(product, pd);
                    if (productCategory != null) {
                        if (productCategory.getIsCreateTicket() != 1) {
                            pd.setIsTimePeriod(-1);
                            pd.setExpiryPointTime(null);
                            pd.setExpiryPeriod(null);
                            pd.setShareContent(null);
                        }
                        if (productCategory.getIsDistribution() != 1) {
                            pd.setIsDistribution(0);
                            pd.setLowerPrice(0D);
                        }
                        if (pd.getIsTimePeriod() != -1) {
                            if (pd.getIsTimePeriod() == 0) {
                                pd.setExpiryPeriod(null);
                            } else {
                                pd.setExpiryPointTime(null);
                            }
                        }
                    }
                    if (isOneProduct.equals("1")) {
                        Config config = configService.getConfigByKey(ConfigEnum.LOAN_RATE);
                        pd.setOneProductApplyStatus(AuditStatusEnum.ING_AUDIT);
                        pd.setOneProductStatus(1);
                        pd.setLoanPrice(ArithmeticUtil.floor(product.getOutPrice() - 1, 2));
                    } else if (isOneProduct.equals("0")) {
                        pd.setOneProductApplyStatus(AuditStatusEnum.NONE_AUDIT);
                        pd.setOneProductStatus(0);
                        pd.setLoanPrice(0D);
                    }
                    if (pd.getIsPresentPoint() == 0) {
                        pd.setGivingEnumber(0);
                        pd.setGivingPnumber(0);
                    }
                    jpaBaseDao.merge(pd);
                } else {//下级商品不同步详情、预览图、是否分销、分销价格、积分赠送数量
                    Product originalProduct = getProductInfo(pd.getId());
                    Product parentProduct = getProductInfo(pd.getParentProductId());
                    double lowerPrice = originalProduct.getLowerPrice();
                    String previewImage = originalProduct.getPreviewImage();
                    List<Body> productDetail = originalProduct.getProductDetail();
                    int isPresentPoint = originalProduct.getIsPresentPoint();
                    int givingEnumber = originalProduct.getGivingEnumber();
                    int givingPnumber = originalProduct.getGivingPnumber();
                    String shareContent = originalProduct.getShareContent();
                    MergeUtil.merge(product, pd);
                    jpaBaseDao.merge(pd);
                    if (productCategory != null) {
                        if (productCategory.getIsCreateTicket() != 1) {
                            updatedDisable(pd.getId());
                        }
                    }
                    Map<String, Object> map = Maps.newHashMap();
                    map.put("organizationId", pd.getOrganizationId());
                    map.put("parentOrganizationId", parentProduct.getOrganizationId());
                    map.put("auditStatus", AuditStatusEnum.HAS_AUDIT);
                    map.put("discountStatus", AuditStatusEnum.HAS_AUDIT);
                    List<Object[]> list = accountRelationService.getAllAssociated(map);
                    if (list != null && list.size() > 0) {
                        Object[] object = list.get(0);
                        AccountRelation accountRelation = (AccountRelation) object[0];
                        pd.setBasePrice(ArithmeticUtil.floor((Float.valueOf(accountRelation.getDiscount()) / 10) * product.getLowerPrice(), 2));
                    } else {
                        pd.setBasePrice(originalProduct.getLowerPrice());
                    }
                    pd.setLowerPrice(lowerPrice);
                    pd.setPreviewImage(previewImage);
                    pd.setShareContent(shareContent);
                    pd.setProductDetail(productDetail);
                    pd.setIsPresentPoint(isPresentPoint);
                    pd.setGivingEnumber(givingEnumber);
                    pd.setGivingPnumber(givingPnumber);

                    jpaBaseDao.merge(pd);
                }
                if (product.getParentProductId() == null) {
                    if (product.getIsDistribution() == 0) {//供应商改分销状态为不可分销时,下架分销商已分销的商品
                        if (pd.getId() != id) {
                            updatedDisable(pd.getId());
                        }
                    }
                }
            }
        }
    }

    @Override
    public float turnStringtoFloat(String str) {
        return (Float.valueOf(str.substring(0, str.length() - 1))) / 100;
    }


    @Override
    public Float rounded(String str, int value) {
        BigDecimal bigDecimal = new BigDecimal(str);
        return bigDecimal.setScale(value, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Transactional
    @Override
    public void updateProductStatus(String ids, String flag) {
        String myId[] = ids.split(",");
        for (int i = 0; i < myId.length; i++) {
            Long id = Long.parseLong(myId[i]);
            Product product = productDao.getEntityById(Product.class, id);
            if (flag.equals("shelves")) {
                updateShelves(product.getId());
            } else if (flag.equals("disable")) {
                updatedDisable(product.getId());
            } else if (flag.equals("oneShelves")) {
                product.setOneProductStatus(2);
            } else if (flag.equals("oneDisable")) {
                product.setOneProductStatus(1);
            } else if (flag.equals("oneRemove")) {
                product.setOneProductApplyStatus(AuditStatusEnum.NONE_AUDIT);
            } else if (flag.equals("delete")) {
                deleteProduct(product.getId());
            } else {
                product.setProductStatus(0);
//                product.setAvailable(AvailableEnum.UNAVAILABLE);
            }
            productDao.merge(product);
        }
    }

    @Override
    public List getDistributionProduct(Long parentAccountId, Long accountId) {
        return productDao.getDistributionProduct(parentAccountId, accountId);
    }


    @Override
    public String urlAssem(String url, Map<String, Object> map) {
        Set<String> parameters = map.keySet();
        Iterator<String> iterator = parameters.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (map.get(key) == null) {
                iterator.remove();//去除map中值为null的元素
            }
        }
        if (map != null || map.size() > 0) {
            Set<String> newParameters = map.keySet();
            Iterator<String> newIterator = newParameters.iterator();
            for (int i = 0; i < newParameters.size(); i++) {
                String parameter = newIterator.next();
                if (i == 0) {
                    url += "?" + parameter + "=" + map.get(parameter);
                } else {
                    url += "&" + parameter + "=" + map.get(parameter);
                }
            }
        }
        return url;
    }

    @Override
    public float computePointAmount(Organization organization, Product product) {
        Config config = configService.getConfigByKey(ConfigEnum.POINT_MIN_RATIO);
        float pointRatio = Float.valueOf(config.getValue());
        float price = rounded(String.valueOf((pointRatio * product.getOutPrice())), 2);
        return price;
    }

    @Override
    public float computeCashbackAmount(Product product) {
        if (product.getCashbackRatio() != null && product.getOutPrice() != null) {
            float price = rounded(String.valueOf((product.getCashbackRatio()) * product.getOutPrice()), 2);
            return price;
        } else {
            return 0F;
        }
    }

    @Override
    public double computeUserLoan(double outPrice, float loan) {
        double userLoan = (outPrice - 1) * loan;

        return ArithmeticUtil.floor(userLoan, 2);
    }

    @Override
    public double givePoint(double outPrice) {
        Config config = configService.getConfigByKey(ConfigEnum.POINT_O_GIVING_RATIO);
        float ratio = Float.parseFloat(config.getValue());
        return ArithmeticUtil.mul(outPrice,ratio);
    }


    /**
     * 更新商品销售数量
     * <p/>
     * ***需要更新购买商品的父类的销售额字段
     *
     * @param purchaseProduct
     * @param amount
     */
    public void updateProductSaleNumber(Product purchaseProduct, Integer amount) {
        String path = purchaseProduct.getPath();
        String[] paramString = path.split("/");
        int i = 1;//从1开始 paramString【0】为空字符
        List<String> params = new ArrayList<String>();
        while ((!paramString[i].equals(purchaseProduct.getId().toString())) && (i < paramString.length)) {
            params.add(paramString[i]);
            i++;
        }
        params.add(purchaseProduct.getId().toString());

        productDao.updateProductSaleNumber(Product.class, params, amount);
    }

    /**
     * 更新商品库存
     * <p/>
     * ***商品库存只存在于该商品的原始商品中
     *
     * @param purchaseProduct
     * @param amount
     */
    public void updateProductStockNumber(Product purchaseProduct, Integer amount) {
        Product originProduct = this.getProductInfo(purchaseProduct.getOriginProductId());
        //库存为-1时代表 无库存上限
        if (originProduct.getStockNumber().equals(-1)) {
            //不修改库存
        } else {
            if (amount > originProduct.getStockNumber())//如果库存不足 则抛出异常
            {
                throw new BaseBusinessException(BizError.PRODUCT_STOCK_NUMBER_IS_NOT_ENOUGH);
            }
//            originProduct.setStockNumber(originProduct.getStockNumber() - amount);
//            jpaBaseDao.merge(originProduct);

            productDao.updateProductStockNumber(originProduct.getId(), amount);
        }

    }

    @Override
    public DomainPage<Map<String, Object>> getOneProductsByPage(Map<String, Object> conditions, DomainPage domainPage) {
        checkNotNull(conditions);
        checkNotNull(domainPage);
        /** 获取商品的基础信息 **/
        DomainPage<Product> searchDomainPage = getBaseProduct(conditions, domainPage);

        /** 完善商品的相关信息 **/
        List<Product> oldProducts = searchDomainPage.getDomains();
        List<Map<String, Object>> newProducts = Lists.newArrayList();
        for (Product product : oldProducts) {
            Map<String, Object> newProduct = Maps.newHashMap();
            Organization organization = organizationService.getOrganizationById(product.getOrganizationId());
            Province province = provinceService.getProvinceByID(organization.getProvinceId());
            newProduct.put("product", product);
            newProduct.put("organization", organization);
            newProduct.put("province", province);

            newProducts.add(newProduct);
        }

        /** 封装最终结果 **/
        long pageSize = searchDomainPage.getPageSize();
        long pageIndex = searchDomainPage.getPageIndex();
        long totalCount = searchDomainPage.getTotalCount();
        DomainPage<Map<String, Object>> returnDomainPage = new DomainPage<Map<String, Object>>(pageSize, pageIndex, totalCount);
        returnDomainPage.setDomains(newProducts);

        return returnDomainPage;
    }

    @Override
    public boolean isLegalData(Product product) {
        Config config = configService.getConfigByKey(ConfigEnum.POINT_P_GIVING_MAX_RATIO);
        boolean flag = true;
        if (product.getParentProductId() == null) {
            //四个价格有一个为负数就为false;
            if (product.getMarketPrice() < 0 || product.getOutPrice() < 0 || product.getGivingEnumber() < 0 || product.getGivingPnumber() < 0) {
                flag = false;
            }
            //售价小于市价false;
            if (product.getOutPrice() - product.getMarketPrice() > 0) {
                flag = false;
            }
            //分销价大于售价false;
            if (product.getOutPrice() != 0) {
                if (product.getOutPrice() - product.getLowerPrice() <= 0) {
                    flag = false;
                }
            }

            int pointNumber = ArithmeticUtil.floor(product.getOutPrice() * Float.valueOf(config.getValue()));
            //应赠最大积分数小于实赠积分数false;
            if (pointNumber - product.getGivingPnumber() < 0 || pointNumber - product.getGivingEnumber() < 0) {
                flag = false;
            }
        } else {
            Product parentProduct = getProductInfo(product.getId());
            if (product.getOutPrice() <= 0 || product.getGivingEnumber() < 0 || product.getGivingPnumber() < 0) {
                flag = false;
            }
            if (product.getOutPrice() - parentProduct.getMarketPrice() > 0) {
                flag = false;
            }
            int pointNumber = ArithmeticUtil.floor(product.getOutPrice() * Float.valueOf(config.getValue()));
            //应赠最大积分数小于实赠积分数false;
            if (pointNumber - product.getGivingPnumber() < 0 || pointNumber - product.getGivingEnumber() < 0) {
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public DomainPage getHasDistributionProductList(Map<String, Object> fieldNameValueMap, Map<String, Object> fuzzyFieldNameValueMap, Map<String, Object> compoundFieldNameValueMap, long pageIndex, long pageSize) {
        DomainPage domainPage = productDao.getHasDistributionProductList(fieldNameValueMap, fuzzyFieldNameValueMap, compoundFieldNameValueMap, pageIndex, pageSize);
        if (domainPage != null && domainPage.getDomains().size() > 0) {
            for (int i = 0; i < domainPage.getDomains().size(); i++) {
                Object[] objects = new Object[4];
                objects[0] = domainPage.getDomains().get(i);
                if (((Product) domainPage.getDomains().get(i)).getParentProductId() != 0) {
                    Product product = productDao.getEntityById(Product.class, ((Product) domainPage.getDomains().get(i)).getOriginProductId());
                    objects[1] = product;
                } else {
                    objects[1] = domainPage.getDomains().get(i);
                }
                objects[2] = organizationService.getOrganizationById(((Product) objects[0]).getOrganizationId());
                Organization organization = organizationService.getOrganizationById(((Product) objects[0]).getOrganizationId());
                objects[3] = provinceService.getProvinceByID(organization.getProvinceId());
                domainPage.getDomains().set(i, objects);
            }
        }
        return domainPage;
    }

    @Override
    public Long getAllProductAmount() {
        return jpaBaseDao.getEntityTotalCount(Product.class);
    }

    @Override
    public Long getShelvesProductAmount() {
        return productDao.getShelvesProductAmount();
    }

    private DomainPage<Product> getBaseProduct(Map<String, Object> conditions, DomainPage domainPage) {
        List<Expression> expressions = Lists.newArrayList();
        String productName = (String) conditions.get("productName");
        Long organizationId = (Long) conditions.get("organizationId");
        Integer productStatus = (Integer) conditions.get("productStatus");
        Integer oneProductStatus = (Integer) conditions.get("oneProductStatus");
        Long provinceId = (Long) conditions.get("provinceId");
        AuditStatusEnum oneProductApplyStatus = (AuditStatusEnum) conditions.get("oneProductApplyStatus");
        // 商品名字
        if (StringUtils.isNotBlank(productName)) {
            Expression expression = new Expression("productName", Operation.AllLike, "%" + productName + "%");
            expressions.add(expression);
        }
        // 商品的一元购状态
        if (oneProductApplyStatus != null) {
            Expression expression = new Expression("oneProductApplyStatus", Operation.Equal, oneProductApplyStatus);
            expressions.add(expression);
        } else {
            List<AuditStatusEnum> auditStatusEnums = Lists.newArrayList(AuditStatusEnum.ING_AUDIT, AuditStatusEnum.HAS_AUDIT, AuditStatusEnum.FAIL_AUDIT);
            Expression expression = new Expression("oneProductApplyStatus", Operation.IN, auditStatusEnums);
            expressions.add(expression);
        }
        // 商户的ID
        if (organizationId != null) {
            Expression expression = new Expression("organizationId", Operation.Equal, organizationId);
            expressions.add(expression);
        }
        // 商品上架状态
        if (productStatus != null) {
            Expression expression = new Expression("productStatus", Operation.Equal, productStatus);
            expressions.add(expression);
        }
        // 一元购商品上架状态
        if (oneProductStatus != null) {
            Expression expression = new Expression("oneProductStatus", Operation.Equal, oneProductStatus);
            expressions.add(expression);
        }
        // 商户属于的省
        if (provinceId != null) {
            List<Organization> organizations = organizationService.getOrganizationByProvinceId(provinceId);
            List<Long> organizationIds = Lists.newArrayList();
            if (organizations.size() > 0) {
                // 该区域商户的ID
                for (Organization organization : organizations) organizationIds.add(organization.getId());
            } else {
                // 使用-1代表该区域无商户,也就是该区域无商品
                organizationIds.add(-1L);
            }
            Expression expression = new Expression("organizationId", Operation.IN, organizationIds);
            expressions.add(expression);
        }

        return productDao.getEntitiesPagesByExpressionList(Product.class, expressions, domainPage.getPageIndex(), domainPage.getPageSize());
    }
}
