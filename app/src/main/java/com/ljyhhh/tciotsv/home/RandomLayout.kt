package com.ljyhhh.tciotsv.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import java.util.Random


//**
//
// * 可以添加随机位置View的布局!
// * 主要的思路是：
// * 1、位置随机
// * 2、防止覆盖
// */

//**
//
// * 可以添加随机位置View的布局!
// * 主要的思路是：
// * 1、位置随机
// * 2、防止覆盖
// */
class RandomLayout<T> : RelativeLayout {
    /**
     * 此列表用于保存随机的View视图
     * 在添加随机View的时候应当判断此视图是否有覆盖的
     * 有的话应该重新进行随机!
     */
    var randomViewList = ArrayList<View>()
    private var onRandomItemClickListener: OnRandomItemClickListener<T>? = null
    var onRandomItemLongClickListener: OnRandomItemLongClickListener<T>? = null
    var isItemClickable = true
    var isItemLongClickable = true
    private var context: Context? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun setContext(context: Context?) {
        this.context = context
    }

    /**
     * 添加到一个随机的XY位置，且不重复。
     */
    fun addViewAtRandomXY(view: View?, t: T) {
        if (view == null) return
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        post(Runnable {
            randomViewList.remove(view)
            // 100次随机上限
            for (i in 0..99) {
                val xy = createXY(
                    view.measuredHeight,
                    view.measuredWidth
                )
                if (randomViewList.size == 0) {
                    addViewAndSetXY(view, xy[0], xy[1], t)
                } else {
                    var isRepeat = false
                    // 迭代已经存在的View，判断是否重叠!
                    for (subView in randomViewList) {
                        // 得到XY
                        val x = subView.x.toInt()
                        val y = subView.y.toInt()
                        val width = subView.measuredWidth
                        val height = subView.measuredHeight
                        // 创建矩形
                        val v1Rect = Rect(x, y, width + x, height + y)
                        val v2Rect = Rect(
                            xy[0], xy[1], view.measuredWidth + xy[0], view.measuredHeight + xy[1]
                        )
                        if (Rect.intersects(v1Rect, v2Rect)) {
                            isRepeat = true
                            break
                        }
                    }
                    if (!isRepeat) {
                        addViewAndSetXY(view, xy[0], xy[1], t)
                        return@Runnable
                    }
                }
            }
        })
    }

    fun addViewAndSetXY(view: View, x: Int, y: Int, t: T) {
        removeView(view)
        addView(view)
        randomViewList.add(view)
        view.x = x.toFloat()
        view.y = y.toFloat()
        // 设置单击事件!
        view.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View) {
                if (onRandomItemClickListener != null && isItemClickable) {
                    onRandomItemClickListener!!.onRandomItemClick(v, t)
                }
            }
        })
        // 设置长按事件!
        view.setOnLongClickListener(object : OnLongClickListener {
            override fun onLongClick(v: View): Boolean {
                return if (onRandomItemLongClickListener != null && isItemLongClickable) onRandomItemLongClickListener!!.onRandomItemLongClick(
                    v,
                    t
                ) else false
            }
        })
    }

    /**
     * 添加一个View到随机列表中，以此达到防止覆盖的效果!
     */
    fun addViewToRandomList(view: View) {
        randomViewList.add(view)
    }

    /**
     * 清除所有的随机视图!
     */
    fun removeAllRandomView() {
        for (v in randomViewList) {
            removeView(v)
        }
        randomViewList.clear()
    }

    /**
     * 从列表中移除一个随机视图!
     */
    fun removeRandomViewFromList(view: View) {
        randomViewList.remove(view)
    }

    /**
     * 随机生成一个 0 到指定区间的值!
     *
     * @param max 0到max但是不包括max
     * @return 同上
     */
    private fun random(max: Int): Int {
        // LogUtils.d("Max是：" + max);
        return Random().nextInt(max)
    }

    /**
     * 根据传入的宽和高返回一个随机的坐标!
     */
    @SuppressLint("NewApi")
    private fun createXY(height: Int, width: Int): IntArray {
        val xyRet = intArrayOf(0, 0)
        // 初始化我们当前布局的屏幕XY!
        val layoutHeight = measuredHeight
        val layoutWidth = measuredWidth
        // 先随机一个X，注意一下就是，X轴是从View的左向右延申的
        // 注意，要减去内部填充!!!
        // LogUtils.d("paddingEnd: " + paddingEnd);
        xyRet[0] = random(
            layoutWidth - (width + paddingStart + paddingEnd)
        )
        // LogUtils.d(" 布局宽度：" + layoutWidth + "，X轴：" + xyRet[0] + "，最终宽度：" + (xyRet[0] + width + paddingEnd + paddingStart));
        // 然后从Y是从View的上向下延申，所以我们需要进行下限值限制，避免越界!
        xyRet[1] = random(
            layoutHeight - (height + paddingBottom + paddingTop)
        )
        return xyRet
    }

    fun getOnRandomItemClickListener(): OnRandomItemClickListener<*>? {
        return onRandomItemClickListener
    }

    fun setOnRandomItemClickListener(onRandomItemClickListener: OnRandomItemClickListener<T>?) {
        this.onRandomItemClickListener = onRandomItemClickListener
    }

    interface OnRandomItemClickListener<T> {
        fun onRandomItemClick(view: View?, t: T)
    }

    interface OnRandomItemLongClickListener<T> {
        fun onRandomItemLongClick(view: View?, t: T): Boolean
    }
}
