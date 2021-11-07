/****************************************************************************************
 * Copyright (c) 2018 Mike Hardy <mike@mikehardy.net>                                   *
 *                                                                                      *
 * This program is free software; you can redistribute it and/or modify it under        *
 * the terms of the GNU General Public License as published by the Free Software        *
 * Foundation; either version 3 of the License, or (at your option) any later           *
 * version.                                                                             *
 *                                                                                      *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY      *
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      *
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.             *
 *                                                                                      *
 * You should have received a copy of the GNU General Public License along with         *
 * this program.  If not, see <http://www.gnu.org/licenses/>.                           *
 ****************************************************************************************/

package com.ichi2.anki.dialogs

import android.os.Bundle
import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.ichi2.anki.R
import com.ichi2.anki.analytics.AnalyticsDialogFragment
import com.ichi2.utils.FunctionalInterfaces

open class IntegerDialog : AnalyticsDialogFragment() {
    private var mConsumer: FunctionalInterfaces.Consumer<Int>? = null
    fun setCallbackRunnable(consumer: FunctionalInterfaces.Consumer<Int>?) {
        mConsumer = consumer
    }

    fun setArgs(title: String?, prompt: String?, digits: Int) {
        setArgs(title, prompt, digits, null)
    }

    fun setArgs(title: String?, prompt: String?, digits: Int, content: String?) {
        val args = Bundle()
        args.putString("title", title)
        args.putString("prompt", prompt)
        args.putInt("digits", digits)
        args.putString("content", content)
        arguments = args
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): MaterialDialog {
        super.onCreate(savedInstanceState)
        var builder = MaterialDialog.Builder(requireActivity())
            .title(requireArguments().getString("title")!!)
            .positiveText(resources.getString(R.string.dialog_ok))
            .negativeText(R.string.dialog_cancel)
            .inputType(InputType.TYPE_CLASS_NUMBER)
            .inputRange(1, requireArguments().getInt("digits"))
            .input(
                requireArguments().getString("prompt"), ""
            ) { _: MaterialDialog?, text: CharSequence -> mConsumer!!.consume(text.toString().toInt()) }
        // builder.content's argument is marked as @NonNull
        // We can't use "" as that creates padding, and want to respect the contract, so only set if not null
        val content = requireArguments().getString("content")
        if (content != null) {
            builder = builder.content(content)
        }
        return builder.show()
    }
}