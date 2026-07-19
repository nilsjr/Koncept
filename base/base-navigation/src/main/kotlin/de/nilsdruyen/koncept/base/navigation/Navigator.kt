package de.nilsdruyen.koncept.base.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack

/**
 * Navigator with per-top-level-destination back stacks so each tab keeps its own state.
 *
 * [backStack] is the flattened list that [androidx.navigation3.ui.NavDisplay] renders: the
 * concatenation of every visited top level tab's stack, in most-recently-selected order. The last
 * entry is the visible screen. [topLevelStackSizes] records how many entries belong to each tab so
 * the flat list can be split back into per-tab stacks.
 *
 * Switching tabs never removes entries, it only reorders them: the selected tab's stack is moved to
 * the end of [backStack]. Because the entries stay in the list, the `NavDisplay` entry decorators
 * keep their `ViewModel`s and `rememberSaveable` state alive, so returning to a tab restores exactly
 * where the user left it (including any screens they had navigated to within that tab).
 */
class Navigator(
    val backStack: NavBackStack<NavKey>,
    private val topLevelStackSizes: MutableList<Int>,
) {

    /** Navigates within the current top level tab. */
    fun goTo(destination: NavKey) {
        backStack.add(destination)
        topLevelStackSizes[topLevelStackSizes.lastIndex]++
    }

    /**
     * Selects a top level destination. If it has been visited before, its saved stack is brought
     * back to the front with all of its state intact; otherwise a fresh stack is started for it.
     * Reselecting the current tab is a no-op.
     */
    fun goToTopLevel(destination: NavKey) {
        val currentIndex = topLevelStackSizes.lastIndex
        val existingIndex = topLevelIndexOf(destination)
        when (existingIndex) {
            currentIndex -> return
            -1 -> {
                backStack.add(destination)
                topLevelStackSizes.add(1)
            }
            else -> moveTopLevelToEnd(existingIndex)
        }
    }

    /** Pops the visible entry. Guards against emptying the stack, which `NavDisplay` forbids. */
    fun goBack() {
        if (backStack.size <= 1) return
        backStack.removeAt(backStack.lastIndex)
        val lastIndex = topLevelStackSizes.lastIndex
        if (--topLevelStackSizes[lastIndex] == 0) {
            topLevelStackSizes.removeAt(lastIndex)
        }
    }

    private fun topLevelStart(index: Int): Int {
        var start = 0
        for (i in 0 until index) start += topLevelStackSizes[i]
        return start
    }

    /** Index of the tab whose root key is [key], or -1 if that tab has not been visited. */
    private fun topLevelIndexOf(key: NavKey): Int {
        var start = 0
        for (i in topLevelStackSizes.indices) {
            if (backStack[start] == key) return i
            start += topLevelStackSizes[i]
        }
        return -1
    }

    private fun moveTopLevelToEnd(index: Int) {
        val start = topLevelStart(index)
        val size = topLevelStackSizes[index]
        val entries = ArrayList<NavKey>(size)
        for (i in 0 until size) entries.add(backStack[start + i])
        repeat(size) { backStack.removeAt(start) }
        topLevelStackSizes.removeAt(index)
        backStack.addAll(entries)
        topLevelStackSizes.add(size)
    }
}

@Composable
fun rememberNavigator(startDestination: NavKey, extraDestinations: List<NavKey> = emptyList()): Navigator {
    val backStack = rememberNavBackStack(startDestination, *extraDestinations.toTypedArray())
    // The start destination and any extra destinations (e.g. a deep link) form the first tab's stack.
    val topLevelStackSizes = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() },
        ),
    ) {
        mutableStateListOf(1 + extraDestinations.size)
    }

    return remember {
        Navigator(backStack = backStack, topLevelStackSizes = topLevelStackSizes)
    }
}
