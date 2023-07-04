package mb.safeEat.services.api.models

data class Restaurant(
    val id: String? = null,
    val name: String? = null,
    val logo: String? = null,
    val cover: String? = null,
    val deliveries: List<Delivery>? = null,
) {
    fun formattedDeliveryPrice(unit: String) = deliveries?.get(0)?.formattedDeliveryPrice(unit)!!

    fun formattedDeliveryInterval() = deliveries?.get(0)?.formattedDeliveryInterval()!!
}
