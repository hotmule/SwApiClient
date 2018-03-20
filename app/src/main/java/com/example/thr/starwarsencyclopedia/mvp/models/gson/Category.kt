package com.example.thr.starwarsencyclopedia.mvp.models.gson

import com.google.gson.annotations.Expose

class Category(@Expose var next: String,
               @Expose var previous: String,
               @Expose var results: ArrayList<ItemBaseDetails>)