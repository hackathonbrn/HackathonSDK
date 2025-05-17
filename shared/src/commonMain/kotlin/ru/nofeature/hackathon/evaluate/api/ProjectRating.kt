package ru.nofeature.hackathon.evaluate.api

interface ProjectRating {
    val project: Project
    val judge: Judge
    val ratings: List<Rating>
}