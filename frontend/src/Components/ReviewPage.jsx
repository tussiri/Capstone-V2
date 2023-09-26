import React, {useState, useEffect, useContext} from "react";
import axios from "axios";
import {useParams, useNavigate,} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faStar} from '@fortawesome/free-solid-svg-icons';
import "../Styles/ReviewPage.css"
import authAxios from "../utility/authAxios";
import {config} from "@fortawesome/fontawesome-svg-core";
import CardContent from "@mui/material/CardContent";
import Card from "@mui/material/Card";
import Button from "@mui/material/Button";
import {UserContext} from "../stores/UserStore";

function ReviewPage() {
    const navigate = useNavigate()
    const {recipeId, userId, reviewId} = useParams();
    const [recipe, setRecipe] = useState()
    const [review, setReview] = useState({
        comment: '',
        rating: ''
    });

    const loggedUserId=localStorage.getItem('userId')
    const {user} = useContext(UserContext)
    console.log("Current user: ", user)

    const [reviews, setReviews] = useState([]);

    const StarRating = ({rating}) => {
        return (
            <div>
                {[...Array(5)].map((star, i) => {
                    const ratingValue = i + 1;
                    return (
                        <FontAwesomeIcon
                            icon={faStar}
                            key={i}
                            className={ratingValue <= rating ? "filled-star" : "empty-star"}
                        />
                    );
                })}
            </div>
        );
    };


    const handleChange = (e, fieldName = null) => {
        if (fieldName) {
            setReview(prevState => ({...prevState, [fieldName]: e}));
        } else {
            const {name, value} = e.target;
            setReview(prevState => ({...prevState, [name]: value}));
        }
    };


    useEffect(() => {
        fetchReviews();
    }, []);

    const fetchReviews = () => {
        console.log(recipeId);
        authAxios
            .get(`${process.env.REACT_APP_BACKEND_URL}/review/recipe/${recipeId}`)
            .then(response => {
                console.log("Response Data: ", response.data)
                setReviews(response.data);

                console.log("User info: ",
                    response.data.map(review => review.user)
                )
            })
            .catch(error => {
                console.error('Error fetching reviews', error);
            });
    }


    const handleSubmit = (e) => {
        const userId = localStorage.getItem('userId');
        if (!userId) {
            alert("You must be logged in to post a review");
            navigate("/login");  // navigate to login page
            return;  // stop the function execution
        }

        console.log(reviewId);
        console.log(userId)
        e.preventDefault();

        // submit the review
        authAxios
            .post(`${process.env.REACT_APP_BACKEND_URL}/review/user/${userId}/recipe/${recipeId}`, review)
            .then(response => {
                // console.log(response.data.data.id);
                fetchReviews();
                navigate(`/recipes/${recipeId}`)
            })
            .catch(error => {
                console.error("Error submitting review", error);
            });
    }
    useEffect(() => {
        authAxios.get(`${process.env.REACT_APP_BACKEND_URL}/recipes/${recipeId}`)
            .then((response) => {
                setRecipe(response.data.data);
            })
            .catch((error) => console.error(`Error fetching recipe id for reviews`, error));
    }, [recipeId])

    return (
        <div>
            <Button sx={{ color: 'white', mt:3 }} variant="contained" onClick={() => navigate(`/recipes/${recipeId}`)}>Back to Recipe</Button>
            <h2>Reviews:</h2>
            {reviews.map(review => (
                <Card key={review.id}>
                    <CardContent>
                        {/*<p>Reviewer: {review.username}</p>*/}
                        <StarRating rating={review.rating}/>
                        <p>{review.comment}</p>
                    </CardContent>
                </Card>
            ))}
            <h2>Leave your review</h2>
            {recipe && recipe.userId.toString() !== loggedUserId && (
                <form onSubmit={handleSubmit}>
                    <label>
                        Comment:
                        <input type='text' name="comment" value={review.comment} onChange={handleChange}/>
                    </label>
                    <label>
                        Rating:
                        <StarRating rating={review.rating}/>
                    </label>
                    <input type="submit" value="Submit"/>
                </form>
            )}
        </div>
    );
}

export default ReviewPage;