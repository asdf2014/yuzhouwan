package com.yuzhouwan.hacker.dsl

def invokeMethod(String name, args) {
    print "<${name}"
    args.each {
        arg ->
            if (arg instanceof Map) {
                arg.each {
                    print " ${it.key} ='${it.value}' "
                }
            } else if (arg instanceof Closure) {
                print '>'
                arg.delegate = this
                def value = arg.call()
                if (value) {
                    print "${value}"
                }
            }
    }
    println "</${name}>"
}

html {
    head {
        meta {
        }
    }
    body {
        table(style: 'margin:2px;') {
            tr('class': 'trClass', style: 'padding:2px;') {
                td { 'http://' }
                td { 'yuzhouwan.' }
                td { 'com' }
            }
        }
    }
}