package com.pemass.persist.domain.jpa.biz;

import com.pemass.common.server.domain.BaseDomain;

import javax.persistence.*;

/**
 * 	商品图片资源
 * @author 向阳
 *
 */
@Entity
@Table(name = "biz_product_image")
public class ProductImage extends BaseDomain{
		
    @Column(name = "product_id",nullable = false)
	private Long productId;//商品

    @Column(name = "title",length = 50)
    private String title;//标题

    @Column(name = "content",length = 400)
    private String content;//描述

    @Column(name = "url",length = 200,nullable = false)
    private String url;//资源URL地址(相对地址)

    @Column(name = "sequence")
    private Double sequence;//序列

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }
}
