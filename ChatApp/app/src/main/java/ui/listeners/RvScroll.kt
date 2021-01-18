package com.mu.jan.sparkchat.ui.listeners

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mu.jan.sparkchat.data.viewModel.ChatActivityViewModel

object RvScroll {
    fun setListener(rv : RecyclerView,vm: ChatActivityViewModel){
        /**
         * Chat scroll
         */
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager : LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastItemVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                var lastItemPosition = vm.messageList.size-1
                /**
                 * Here, we assume if last 5 item or one of last 5 items are visible, that means user
                 * at bottom, you can scroll when new msg received, But when this condition reverse,
                 * that means user not at bottom, because may be user is seeing messages, no need to
                 * scroll when new msg received,
                 */
                if(lastItemPosition>=5){
                    //assume
                    lastItemPosition -= 5
                    vm.isUserNeedScrollStateBottom = lastItemVisiblePosition>=lastItemPosition
                    vm.isUserNeedSmoothScrollStateBottom = false
                }else {
                    /**
                     * When list items less than 5,no need to scroll,
                     * But In this case, Maybe keyboard hide some items, then rv need
                     * smooth scroll, because when keyboard remain open, only scroll
                     * not works at all.
                     */
                    //
                    vm.isUserNeedScrollStateBottom = false
                    vm.isUserNeedSmoothScrollStateBottom = true

                }
            }
        })
        /**
         * Scroll recycler view up when keyboard opens
         */
        rv.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if(bottom<oldBottom){
                try{  rv.smoothScrollToPosition(rv.adapter!!.itemCount-1) }catch(e:Exception){}
            }
        }
    }

}
