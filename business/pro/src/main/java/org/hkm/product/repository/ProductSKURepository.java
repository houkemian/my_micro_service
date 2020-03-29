package org.hkm.product.repository;
//
//import org.hkm.product.entity.ProductSKU;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.transaction.annotation.Transactional;
//
//@org.springframework.stereotype.Repository
//public interface ProductSKURepository extends CrudRepository<ProductSKU, Long> {
//
//
//    @Query(value="SELECT * from product_sku where id=?1", nativeQuery = true)
//    ProductSKU getById(Long skuId);
//
//    @Query(value="SELECT stock from product_sku where id=?1", nativeQuery = true)
//    float getStockById(Long skuId);
//
//    @Transactional
//    @Modifying
//    @Query(value="update product_sku set stock=?2-?3 where id=?1 and stock=?2", nativeQuery = true)
//    int updateStock(long id, float now, int reduce);
//
//}
