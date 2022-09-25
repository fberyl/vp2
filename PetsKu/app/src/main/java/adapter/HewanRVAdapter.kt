package adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elflin.petsku.R
import com.elflin.petsku.databinding.CardAnimalBinding
import listener.CardAnimalListener
import model.GlobalVar
import model.Hewan

class HewanRVAdapter (val ListDataHewan: ArrayList<Hewan>, val cardListener: CardAnimalListener):
    RecyclerView.Adapter<HewanRVAdapter.viewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.card_animal, parent, false)
        return viewHolder(view, cardListener)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.setData(ListDataHewan[position], position)
    }

    override fun getItemCount(): Int {
        return ListDataHewan.size
    }

    class viewHolder(val itemView: View, val cardListener1: CardAnimalListener): RecyclerView.ViewHolder(itemView) {

        val viewBind = CardAnimalBinding.bind(itemView)

        fun setData(hewan: Hewan, position: Int) {
            viewBind.CardNamaHewan.text = hewan.NamaHewan
            viewBind.CardJenisHewan.text = hewan.JenisHewan
            viewBind.CardUmurHewan.text = hewan.umurHewan.toString()+" tahun"
            viewBind.CardButtonEdit.setOnClickListener {
                cardListener1.OnEditClicked(position)
            }
            viewBind.CardButtonDelete.setOnClickListener{
                cardListener1.OnDeleteClicked(position)
            }


            if(hewan.FotoHewan != "null") {
                val bArray = GlobalVar.StringToByteArr(hewan.FotoHewan)
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                options.inScaled = true
                val bitMap = BitmapFactory.decodeByteArray(
                    bArray,
                    0,
                    bArray.size,
                    options
                )
                viewBind.CardPicture.setImageBitmap(bitMap)
            }
        }
    }
}