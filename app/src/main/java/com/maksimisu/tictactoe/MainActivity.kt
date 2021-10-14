package com.maksimisu.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maksimisu.tictactoe.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.random.Random

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rootView: View

    // Board one buttons
    private lateinit var btnOneBoardOne: FloatingActionButton
    private lateinit var btnTwoBoardOne: FloatingActionButton
    private lateinit var btnThreeBoardOne: FloatingActionButton
    private lateinit var btnFourBoardOne: FloatingActionButton
    private lateinit var btnFiveBoardOne: FloatingActionButton
    private lateinit var btnSixBoardOne: FloatingActionButton
    private lateinit var btnSevenBoardOne: FloatingActionButton
    private lateinit var btnEightBoardOne: FloatingActionButton
    private lateinit var btnNineBoardOne: FloatingActionButton

    // Board two buttons
    private lateinit var btnOneBoardTwo: FloatingActionButton
    private lateinit var btnTwoBoardTwo: FloatingActionButton
    private lateinit var btnThreeBoardTwo: FloatingActionButton
    private lateinit var btnFourBoardTwo: FloatingActionButton
    private lateinit var btnFiveBoardTwo: FloatingActionButton
    private lateinit var btnSixBoardTwo: FloatingActionButton
    private lateinit var btnSevenBoardTwo: FloatingActionButton
    private lateinit var btnEightBoardTwo: FloatingActionButton
    private lateinit var btnNineBoardTwo: FloatingActionButton

    // Board states
    // 0 for unused
    // 1 for checked by player
    // 2 for checked by computer
    private var boardOneStates: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var boardTwoStates: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    // Buttons lists
    private lateinit var boardOneViews: MutableList<FloatingActionButton>
    private lateinit var boardTwoViews: MutableList<FloatingActionButton>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initVariables()
        setContentView(rootView)

        // Board one

        btnOneBoardOne.setOnClickListener {
            startProcess(1, 0, boardOneStates, boardOneViews, 1)
        }

        btnTwoBoardOne.setOnClickListener {
            startProcess(1, 1, boardOneStates, boardOneViews, 1)
        }

        btnThreeBoardOne.setOnClickListener {
            startProcess(1, 2, boardOneStates, boardOneViews, 1)
        }

        btnFourBoardOne.setOnClickListener {
            startProcess(1, 3, boardOneStates, boardOneViews, 1)
        }

        btnFiveBoardOne.setOnClickListener {
            startProcess(1, 4, boardOneStates, boardOneViews, 1)
        }

        btnSixBoardOne.setOnClickListener {
            startProcess(1, 5, boardOneStates, boardOneViews, 1)
        }

        btnSevenBoardOne.setOnClickListener {
            startProcess(1, 6, boardOneStates, boardOneViews, 1)
        }

        btnEightBoardOne.setOnClickListener {
            startProcess(1, 7, boardOneStates, boardOneViews, 1)
        }

        btnNineBoardOne.setOnClickListener {
            startProcess(1, 8, boardOneStates, boardOneViews, 1)
        }

        // Board two

        btnOneBoardTwo.setOnClickListener {
            startProcess(2, 0, boardTwoStates, boardTwoViews, 1)
        }

        btnTwoBoardTwo.setOnClickListener {
            startProcess(2, 1, boardTwoStates, boardTwoViews, 1)
        }

        btnThreeBoardTwo.setOnClickListener {
            startProcess(2, 2, boardTwoStates, boardTwoViews, 1)
        }

        btnFourBoardTwo.setOnClickListener {
            startProcess(2, 3, boardTwoStates, boardTwoViews, 1)
        }

        btnFiveBoardTwo.setOnClickListener {
            startProcess(2, 4, boardTwoStates, boardTwoViews, 1)
        }

        btnSixBoardTwo.setOnClickListener {
            startProcess(2, 5, boardTwoStates, boardTwoViews, 1)
        }

        btnSevenBoardTwo.setOnClickListener {
            startProcess(2, 6, boardTwoStates, boardTwoViews, 1)
        }

        btnEightBoardTwo.setOnClickListener {
            startProcess(2, 7, boardTwoStates, boardTwoViews, 1)
        }

        btnNineBoardTwo.setOnClickListener {
            startProcess(2, 8, boardTwoStates, boardTwoViews, 1)
        }
    }

    private fun startProcess(boardNumber: Int, btnNumber: Int, boardStates: MutableList<Int>, buttons: List<FloatingActionButton>, newState: Int) {
        if(checkBtnState(btnNumber, boardStates)) {
            GlobalScope.launch { changeState(buttons[btnNumber], btnNumber, boardStates, newState) }
            if(checkBoardForVictory(boardStates)) {
                Toast.makeText(
                    this@MainActivity,
                    "Game is finished. Restarting the board.",
                    Toast.LENGTH_SHORT
                ).show()
                refreshGameViews(buttons)
                runBlocking {
                    refreshStates(boardStates)
                }
            } else {
                GlobalScope.launch {
                    makeChoice(buttons, boardStates, boardNumber)
                    if(checkBoardForVictory(boardStates)) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@MainActivity,
                                "Game is finished. Restarting the board.",
                                Toast.LENGTH_SHORT
                            ).show()
                            refreshGameViews(buttons)
                        }
                        refreshStates(boardStates)
                    }
                }
            }
        }
    }

    private fun refreshGameViews(views: List<FloatingActionButton>) {
        for(i in views) {
            i.setImageResource(R.drawable.ic_circle)
            i.isEnabled = true
        }
    }

    private suspend fun refreshStates(states: MutableList<Int>) {
        coroutineScope {
            launch(Dispatchers.IO) {
                for(i in states.indices) {
                    states[i] = 0
                }
            }
        }
    }

    private suspend fun changeState(btn: FloatingActionButton, btnNumber: Int, states: MutableList<Int>, newState: Int) {
        coroutineScope {
            withContext(Dispatchers.Main) {
                states[btnNumber] = newState
                if (newState == 1) {
                    btn.setImageResource(R.drawable.ic_plus)
                } else {
                    btn.setImageResource(R.drawable.ic_close)
                }
                btn.isEnabled = false
            }
        }
    }

    private fun checkBtnState(btnNumber: Int, states: MutableList<Int>): Boolean {
        return states[btnNumber] == 0
    }

    private fun checkBoardForVictory(states: MutableList<Int>): Boolean {
        return states[0] == states[1] && states[1] == states[2] && states[0] != 0 ||
                states[3] == states[4] && states[4] == states[5] && states[3] != 0 ||
                states[6] == states[7] && states[7] == states[8] && states[6] != 0 ||
                states[0] == states[3] && states[3] == states[6] && states[0] != 0 ||
                states[1] == states[4] && states[4] == states[7] && states[1] != 0 ||
                states[2] == states[5] && states[5] == states[8] && states[2] != 0 ||
                states[0] == states[4] && states[4] == states[8] && states[0] != 0 ||
                states[6] == states[4] && states[4] == states[2] && states[6] != 0
    }

    private suspend fun disableBoard(board: List<FloatingActionButton>) {
        for(i in board) {
            i.isEnabled = false
        }
    }

    private suspend fun enableBoard(board: List<FloatingActionButton>) {
        for(i in board) {
            i.isEnabled = true
        }
    }

    private suspend fun makeChoice(views: List<FloatingActionButton>, states: MutableList<Int>, board: Int) {
        withContext(Dispatchers.Main) {
            disableBoard(views)
        }
        coroutineScope {
            launch(Dispatchers.IO) {
                val first = 1000L
                val second = 10000L
                delay(2000L)
                delay(randomNumber(first, second))
                var condition = true
                while(condition) {
                    val number = randomNumber(0, views.size - 1)
                    if(states[number] == 0) {
                        condition = false
                        var btn: FloatingActionButton
                        if(board == 1) {
                            btn = when(number) {
                                0 -> btnOneBoardOne
                                1 -> btnTwoBoardOne
                                2 -> btnThreeBoardOne
                                3 -> btnFourBoardOne
                                4 -> btnFiveBoardOne
                                5 -> btnSixBoardOne
                                6 -> btnSevenBoardOne
                                7 -> btnEightBoardOne
                                8 -> btnNineBoardOne
                                else -> btnOneBoardOne
                            }
                        } else {
                            btn = when(number) {
                                0 -> btnOneBoardTwo
                                1 -> btnTwoBoardTwo
                                2 -> btnThreeBoardTwo
                                3 -> btnFourBoardTwo
                                4 -> btnFiveBoardTwo
                                5 -> btnSixBoardTwo
                                6 -> btnSevenBoardTwo
                                7 -> btnEightBoardTwo
                                8 -> btnNineBoardTwo
                                else -> btnOneBoardTwo
                            }
                        }
                        changeState(btn, number, states, 2)
                    }
                }
            }
        }
        withContext(Dispatchers.Main) {
            enableBoard(views)
        }
    }

    private fun randomNumber(first: Long, second: Long): Long {
        return Random.nextLong(first, second)
    }

    private fun randomNumber(first: Int, second: Int): Int {
        return Random.nextInt(first, second)
    }

    private fun initVariables() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        rootView = binding.root

        // Board one buttons
        btnOneBoardOne = binding.btnOneBoardOne
        btnTwoBoardOne = binding.btnTwoBoardOne
        btnThreeBoardOne = binding.btnThreeBoardOne
        btnFourBoardOne = binding.btnFourBoardOne
        btnFiveBoardOne = binding.btnFiveBoardOne
        btnSixBoardOne = binding.btnSixBoardOne
        btnSevenBoardOne = binding.btnSevenBoardOne
        btnEightBoardOne = binding.btnEightBoardOne
        btnNineBoardOne = binding.btnNineBoardOne

        // Board two buttons
        btnOneBoardTwo = binding.btnOneBoardTwo
        btnTwoBoardTwo = binding.btnTwoBoardTwo
        btnThreeBoardTwo = binding.btnThreeBoardTwo
        btnFourBoardTwo = binding.btnFourBoardTwo
        btnFiveBoardTwo = binding.btnFiveBoardTwo
        btnSixBoardTwo = binding.btnSixBoardTwo
        btnSevenBoardTwo = binding.btnSevenBoardTwo
        btnEightBoardTwo = binding.btnEightBoardTwo
        btnNineBoardTwo = binding.btnNineBoardTwo

        // Buttons lists
        boardOneViews = mutableListOf(
            btnOneBoardOne,
            btnTwoBoardOne,
            btnThreeBoardOne,
            btnFourBoardOne,
            btnFiveBoardOne,
            btnSixBoardOne,
            btnSevenBoardOne,
            btnEightBoardOne,
            btnNineBoardOne
        )
        boardTwoViews = mutableListOf(
            btnOneBoardTwo,
            btnTwoBoardTwo,
            btnThreeBoardTwo,
            btnFourBoardTwo,
            btnFiveBoardTwo,
            btnSixBoardTwo,
            btnSevenBoardTwo,
            btnEightBoardTwo,
            btnNineBoardTwo
        )
    }
}