package com.example.bricklist

object ProjectsList {
    var projects: MutableList<Project> = mutableListOf<Project>()
    var projectNames: MutableList<String> = mutableListOf<String>()
    var projectsUrl = "http://fcds.cs.put.poznan.pl/MyWeb/BL/"
    var ifVisible: Boolean = true
}