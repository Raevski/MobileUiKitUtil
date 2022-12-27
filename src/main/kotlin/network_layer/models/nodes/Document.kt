package network_layer.models.nodes

data class Document(val id: String,
    val name: String,
    val fills: List<Paint>,
    val style: TypeStyle?)
