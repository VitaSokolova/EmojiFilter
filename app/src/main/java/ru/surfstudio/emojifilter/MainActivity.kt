package ru.surfstudio.emojifilter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ru.surfstudio.emojifilter.filters.EmojiHardcodedListFilter
import ru.surfstudio.emojifilter.filters.EmojiUnicodeGroupFilter

class MainActivity : AppCompatActivity() {

    private lateinit var filterWorkResultsTv: TextView
    private lateinit var surrogateFilterBtn: Button
    private lateinit var hardcodedListFilterBtn: Button
    private lateinit var unicodeGroupFilterBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViews()
        initListeners()
    }


    private fun findViews() {
        filterWorkResultsTv = findViewById(R.id.emoji_tv)
        surrogateFilterBtn = findViewById(R.id.emoji_filter_surrogate_btn)
        hardcodedListFilterBtn = findViewById(R.id.emoji_filter_hardcoded_list_btn)
        unicodeGroupFilterBtn = findViewById(R.id.emoji_filter_unicode_group_btn)
    }

    private fun initListeners() {
        surrogateFilterBtn.setOnClickListener {
            applyFilterToAllEmojis { emojiStr ->
                // see https://stackoverflow.com/questions/22990870/how-to-disable-emoji-from-being-entered-in-android-edittext
                emojiStr.filter { character ->
                    val type = Character.getType(character).toByte()
                    type != Character.SURROGATE && type != Character.OTHER_SYMBOL
                }
            }
        }
        hardcodedListFilterBtn.setOnClickListener {
            applyFilterToAllEmojis { emojiStr -> EmojiHardcodedListFilter.removeEmoji(emojiStr) }
        }
        unicodeGroupFilterBtn.setOnClickListener {
            applyFilterToAllEmojis { emojiStr -> EmojiUnicodeGroupFilter.removeEmoji(emojiStr) }
        }
    }

    private fun applyFilterToAllEmojis(filter: (String) -> String) {
        var filteredString = ""
        ALL_EMOJI_LIST.forEach { emojiStr ->
            filteredString += "${filter(emojiStr)} "
        }
        filterWorkResultsTv.text = filteredString
    }
}