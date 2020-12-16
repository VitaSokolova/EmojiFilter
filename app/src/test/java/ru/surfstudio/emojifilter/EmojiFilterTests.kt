package ru.surfstudio.v_textfield

import org.junit.Test
import ru.surfstudio.emojifilter.ALL_EMOJI_LIST
import ru.surfstudio.v_textfield.utils.EmojiUnicodeGroupFilter

/**
 * Набор тестов для проверки фильтра эмоджи в текстовых полях
 */
class EmojiFilterTests {

    @Test
    fun testEmojiPredicate() {
        val result = ALL_EMOJI_LIST.all { emojiStr ->
            EmojiUnicodeGroupFilter.containsEmoji(emojiStr)
        }
        assert(result)
    }

    @Test
    fun testEmojiFilter() {
        var filteredString = ""
        val result = ALL_EMOJI_LIST.all { emojiStr ->
            filteredString = EmojiUnicodeGroupFilter.removeEmoji(emojiStr)
            filteredString.isEmpty()
        }
        assert(result) { "$filteredString is not empty" }
    }
}
