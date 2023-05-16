import React from "react";

const FoodCard = ({ title, time, image }) => {
    return (
        <div className="FoodCard">
            <img alt="Image of food" src={image}></img>
            <div className="BottomBar">
            <span className="title">{title}</span>
            <span className="TitleTime">{time}</span>
            </div>
        </div>

    )
}

export default FoodCard