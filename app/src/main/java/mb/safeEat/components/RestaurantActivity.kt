package mb.safeEat.components

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R

data class RestaurantParams(
    val restaurant: String,
    val deliveryPrice: String,
    val deliveryTime: String,
)

class RestaurantActivity(
    private val navigation: NavigationListener,
    private val params: RestaurantParams,
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_restaurant, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        initAdapter(view)
        initScreenEvents(view)
    }

    private fun initScreenEvents(view: View) {
        val posterImage = view.findViewById<ImageView>(R.id.restaurant_poster_image)
        val deliveryInterval = view.findViewById<TextView>(R.id.restaurant_delivery_interval)
        val deliveryPrice = view.findViewById<TextView>(R.id.restaurant_delivery_price)
        val restaurantName = view.findViewById<TextView>(R.id.restaurant_name)
        val restaurantImage = view.findViewById<ImageView>(R.id.restaurant_image)
        val searchButton = view.findViewById<MaterialCardView>(R.id.restaurant_search_button)
        val backButton = view.findViewById<MaterialCardView>(R.id.restaurant_back_button)

        posterImage.setImageResource(R.drawable.sandwich)
        deliveryInterval.text = params.deliveryTime
        deliveryPrice.text = params.deliveryPrice
        restaurantName.text = params.restaurant
        restaurantImage.setImageResource(R.drawable.restaurant)
        searchButton.setOnClickListener { navigation.navigateTo(SearchProductActivity(navigation)) }
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initAdapter(view: View) {
        val items = view.findViewById<RecyclerView>(R.id.restaurant_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = RestaurantCategoryAdapter(navigation, createList())
    }

    private fun createList(): ArrayList<RestaurantCategory> {
        return arrayListOf(
            RestaurantCategory(
                "Pizza", arrayListOf(
                    RestaurantProduct(R.drawable.pizza, "Moda da casa", "€2,99", true),
                    RestaurantProduct(R.drawable.pizza, "Presunto e Queijo", "€2,99", false),
                    RestaurantProduct(R.drawable.pizza, "Marguerita", "€2,99", false),
                    RestaurantProduct(R.drawable.pizza, "Frango com Catupiri", "€2,99", false),
                )
            ), RestaurantCategory(
                "Sandwich", arrayListOf(
                    RestaurantProduct(R.drawable.sandwich, "Misto Quente", "€2,99", false),
                    RestaurantProduct(R.drawable.sandwich, "X-Tudo", "€2,99", true),
                    RestaurantProduct(R.drawable.sandwich, "X-Salada", "€2,99", false),
                    RestaurantProduct(R.drawable.sandwich, "X-Bacon", "€2,99", false),
                )
            ), RestaurantCategory(
                "Drinks", arrayListOf(
                    RestaurantProduct(R.drawable.drinks, "Coca-Cola", "€2,99", false),
                    RestaurantProduct(R.drawable.drinks, "Sprite", "€2,99", true),
                    RestaurantProduct(R.drawable.drinks, "Cerveja", "€2,99", false),
                )
            )
        )
    }
}

class RestaurantCategoryAdapter(
    private val navigation: NavigationListener,
    private val data: ArrayList<RestaurantCategory>,
) : RecyclerView.Adapter<RestaurantCategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(
        private val navigation: NavigationListener,
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val category = itemView.findViewById<TextView>(R.id.restaurant_category_name)
        private val items = itemView.findViewById<RecyclerView>(R.id.restaurant_category_items)
        fun bind(item: RestaurantCategory) {
            category.text = item.name
            initAdapter(item)
        }

        private fun initAdapter(item: RestaurantCategory) {
            val adapter = RestaurantProductAdapter(navigation, item.products)
            items.layoutManager = LinearLayoutManager(itemView.context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
            items.adapter = adapter
        }
    }
}

class RestaurantProductAdapter(
    private val navigation: NavigationListener,
    private val data: ArrayList<RestaurantProduct>,
) :
    RecyclerView.Adapter<RestaurantProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant_product, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val navigation: NavigationListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container =
            itemView.findViewById<MaterialCardView>(R.id.restaurant_product_container)
        private val product = itemView.findViewById<TextView>(R.id.restaurant_product_name)
        private val price = itemView.findViewById<TextView>(R.id.restaurant_product_price)
        private val image = itemView.findViewById<ImageView>(R.id.restaurant_product_image)
        private val warning = itemView.findViewById<ImageView>(R.id.restaurant_product_warning)

        fun bind(item: RestaurantProduct) {
            product.text = item.name
            price.text = item.price
            image.setImageResource(item.imageId)

            if (item.restricted) {
                warning.visibility = ImageView.VISIBLE
                container.strokeColor = ContextCompat.getColor(itemView.context, R.color.red)
                container.strokeWidth = dp2px(itemView.context, 2.0f).toInt()
            } else {
                warning.visibility = ImageView.GONE
                container.strokeColor =
                    ContextCompat.getColor(itemView.context, android.R.color.transparent)
                container.strokeWidth = dp2px(itemView.context, 0f).toInt()
            }
            container.setOnClickListener { navigation.navigateTo(ProductDetailsActivity(navigation)) }
        }
    }
}

fun dp2px(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    )
}

data class RestaurantProduct(
    val imageId: Int,
    val name: String,
    val price: String,
    val restricted: Boolean,
)

data class RestaurantCategory(
    val name: String,
    val products: ArrayList<RestaurantProduct>,
)