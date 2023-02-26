package eu.manuelgc.baradmin.model

sealed class DataItem {
    abstract val groupId: String
    abstract val subgroupId: String
    abstract val productId: String
    abstract val name: String
    abstract val imageUrl: String
    abstract val sequence: Int


//    data class GroupItem(val item: Group): DataItem() {
//        override val groupId = item.groupId
//        override val name = item.name
//        override val description = item.description
//        override val imageUrl = item.imageUrl
//        override val sequence = item.sequence
//    }

//    data class ProductItem(val item: Product): DataItem() {
//        override val groupId = item.groupId
//        val subgroupId = item.subgroupId
//        val productId = item.productId
//        val product1Id = item.product1Id
//        val product2Id = item.product2Id
//        val product3Id = item.product3Id
//        val product4Id = item.product4Id
//        override val name = item.name
//        override val description = item.description
//        override val imageUrl = item.imageUrl
//        override val sequence = item.sequence
//    }
    override fun toString() = "$groupId||$subgroupId||$productId||$name||$imageUrl||$sequence"
}

data class ItemFilter(val groupId: String, val subgroupId: String, val productId: String)