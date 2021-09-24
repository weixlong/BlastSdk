package com.hzjq.core.cmd

class Cmd {

    var keyOk:String
    var keyError:String
    var cmd:String
    var key: Key?=null
    var minAckLength = 0


    constructor(keyOk: String, keyError: String, cmd: String) {
        this.keyOk = keyOk
        this.keyError = keyError
        this.cmd = cmd
    }

    constructor(keyOk: String, keyError: String, cmd: String, minAckLength: Int) {
        this.keyOk = keyOk
        this.keyError = keyError
        this.cmd = cmd
        this.minAckLength = minAckLength
    }


    class Key{
        var start = ""
        var key = ""
        var startIndex = 0
        var endIndex = 0

        constructor(start: String, key: String, startIndex: Int, endIndex: Int) {
            this.start = start
            this.key = key
            this.startIndex = startIndex
            this.endIndex = endIndex
        }
    }
}