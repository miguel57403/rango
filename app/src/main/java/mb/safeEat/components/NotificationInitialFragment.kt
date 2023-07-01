package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R

class NotificationInitialFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notification_initial, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(view)
    }

    private fun initAdapter(view: View) {
        val adapter = NotificationAdapter(navigation, createList())
        val items = view.findViewById<RecyclerView>(R.id.notification_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = adapter
    }

    private fun createList(): ArrayList<Notification> {
        return arrayListOf(
            Notification(
                "Sabor Brasileiro",
                "30 seconds ago",
                R.drawable.restaurant,
                "Your order has arrived",
                OrderStatus.DELIVERED
            ),
            Notification(
                "Sabor Brasileiro",
                "5 min ago",
                R.drawable.restaurant,
                "Your order is out for delivery",
                OrderStatus.TRANSPORTING
            ),
            Notification(
                "Sabor Brasileiro",
                "15 min ago",
                R.drawable.restaurant,
                "Your order is in preparation",
                OrderStatus.PREPARING
            ),
            Notification(
                "Mimo's Pizza",
                "1 day ago",
                R.drawable.restaurant,
                "Promotion message",
                null
            ),
            Notification(
                "Gelados Maravilhosos",
                "2 days ago",
                R.drawable.restaurant,
                "Promotion message",
                null
            ),
            Notification(
                "Sabor Brasileiro",
                "25 Apr at 12:45",
                R.drawable.restaurant,
                "Promotion message",
                null
            ),
            Notification(
                "Sabor Brasileiro",
                "23 Apr at 12:45",
                R.drawable.restaurant,
                "Promotion message",
                null
            ),
        )
    }
}

class NotificationAdapter(
    private val navigation: NavigationListener,
    private val data: ArrayList<Notification>
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val navigation: NavigationListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container =
            itemView.findViewById<MaterialCardView>(R.id.restaurant_product_container)
        private val image = itemView.findViewById<ImageView>(R.id.notification_restaurant_image)
        private val restaurant = itemView.findViewById<TextView>(R.id.notification_restaurant_name)
        private val timeArrival = itemView.findViewById<TextView>(R.id.notification_time_arrival)
        private val status = itemView.findViewById<TextView>(R.id.notification_order_status)

        fun bind(item: Notification) {
            image.setImageResource(item.imageId)
            restaurant.text = item.restaurant
            timeArrival.text = item.arrivalTime
            status.text = item.message
            if (item.orderStatus != null) {
                container.setOnClickListener {
                    navigation.navigateTo(
                        OrderDetailFragment(
                            navigation, OrderDetailParams(
                                item.orderStatus,
                                item.restaurant,
                                item.arrivalTime
                            )
                        )
                    )
                }
            } else {
                container.isClickable = false
            }
        }
    }
}

data class Notification(
    val restaurant: String,
    val arrivalTime: String,
    val imageId: Int,
    val message: String,
    val orderStatus: OrderStatus?,
)
