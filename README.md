# ListTask

List of Trending Repositories

This Project contains Android Architecture Components ( LiveData , View Model and MVVM with retrofit for consuming rest api )

# About This Project

In this project we will build a app for List the Trending Repositories using android architecture components ( LiveData and ViewModel ) usning MVVM pattern by the help of Retrofit for consuming rest api data from 

this API  https://private-anon-fa7096e636-githubtrendingapi.apiary-mock.com/repositories

# App Features

Show the List of Trending Repositories.

# Overview of Decisions Made

 1. After installed this application and if internet is available in the mobile , the very first time opening the 

    app , I wrote the code to fetch the data from API and Put the data in Local database and 

    Loading the data into the recyclerview.

 2.  If I swipe the Pull to refresh button , I wrote the code to

     fetch the data again from the Api and stored in Local database and Loading List.

 3.  After fetching the data from Api , the data will be listed on the recyclerview. Now if the internet connection is not

     available , I am fetching the data from Local database when I am opening the App.

 4.  After installed this application and If the Internet connection is not available , the very first time opening the 

     app , I am showing the Error message "No Netwrok connection" and the Try again Button. Now if internet connection is

     available , I click the Try again button , fetched the data from API and Load the data in the List.

 5. When searching the list , I am getting the search results data from the Local Database.

# Library Used

Live Data ,
View Model ,
Retrofit ,
Glide ,
RecyclerView,
Gson ,
room


