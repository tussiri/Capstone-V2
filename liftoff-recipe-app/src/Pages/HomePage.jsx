import React from "react";
import FoodCard from "../Components/FoodCard"
import ChickenPic from "../Assets/Rectangle 65.png"
import PastaPic from "../Assets/Rectangle 66.png"
import TacoPic from "../Assets/Rectangle 67.png"
import ChocoPic from "../Assets/Rectangle 68.png"
import TortaPic from "../Assets/Rectangle 69.png"
import TirraPic from "../Assets/Rectangle 70.png"
import NavBar from "../Components/NavBar"

function HomePage() {
return(
    <>
    <NavBar />

    <FoodCard title='Chicken' time='30 min' image={ChickenPic}></FoodCard>
    <FoodCard title='Beef Pasta' time='20 min' image={PastaPic}></FoodCard>
    <FoodCard title='Birria Tacos' time='25 min' image={TacoPic}></FoodCard>
    <FoodCard title='Chocolate Brioche' time='30 min' image={ChocoPic}></FoodCard>
    <FoodCard title='Torta Caprese' time='45 min' image={TortaPic}></FoodCard>
    <FoodCard title='Tirramousa' time='15 min' image={TirraPic}></FoodCard>

    </>
)
}

export default HomePage;