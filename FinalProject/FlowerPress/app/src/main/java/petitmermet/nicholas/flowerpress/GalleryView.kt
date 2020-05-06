package petitmermet.nicholas.flowerpress


import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_gallery_view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class GalleryView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_gallery_view, container, false)


        val box = ObjectBox.boxStore.boxFor(Flower::class.java)
        viewManager = GridLayoutManager(activity, 2)
//        viewManager = LinearLayoutManager(activity)
        viewAdapter = MyAdapter(box.getAll(), activity as MainActivity)

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        return view
    }
}
class MyAdapter(private val myDataset: List<Flower>, private val activity: MainActivity) :


    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item, parent, false) as ImageView
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(imageView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.imageView.setImageURI(Uri.parse(myDataset[position].uri))
        activity
        holder.imageView.setOnClickListener{
            val bundle:Bundle = Bundle()
            bundle.putString("activity", "gallery")
            bundle.putLong("id", myDataset[position].id)
            bundle.putBoolean("edit", true)
            val frag = CreateFlower()
            frag.arguments = bundle
            activity.replaceFragment(frag)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}