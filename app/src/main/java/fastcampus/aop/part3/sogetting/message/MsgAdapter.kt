package fastcampus.aop.part3.sogetting.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import fastcampus.aop.part3.sogetting.R
import fastcampus.aop.part3.sogetting.auth.UserDataModel
class MsgAdapter(val context : Context, val items : MutableList<MsgModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView
        if (convertView == null) {

            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.list_view_item, parent, false)

        }

        val nicknameArea = convertView!!.findViewById<TextView>(R.id.listViewItemNickNameArea)
        val textArea = convertView!!.findViewById<TextView>(R.id.listViewItemNickName)

        nicknameArea.text = items[position].senderInfo
        textArea.text = items[position].sendTxt

        return convertView!!

    }
}