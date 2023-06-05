import React, {useState, useEffect} from "react";
import axios from "axios";
import {useParams, useNavigate,} from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar } from '@fortawesome/free-solid-svg-icons';
import "../Styles/ReviewPage.css"
import authAxios from "../utility/authAxios";
import {config} from "@fortawesome/fontawesome-svg-core";

function ReviewPage() {
    const navigate = useNavigate()
    const {recipeId, userId, reviewId} = useParams();
    const [review, setReview] = useState({
        comment: '',
        rating: ''
    });

    const [reviews, setReviews] = useState([]);

    const StarRating = ({ handleChange }) => {
        return (
            <div>
                {[...Array(5)].map((star, i) => {
                    const ratingValue = i + 1;
                    return (
                        <label key={i}>
                            <input
                                type="radio"
                                name="rating"
                                value={ratingValue}
                                onChange={() => handleChange(ratingValue, 'rating')}
                            />
                            <FontAwesomeIcon icon={faStar} />
                        </label>
                    );
                })}
            </div>
        );
    };


    const handleChange = (e, fieldName = null) => {
        if (fieldName) {
            setReview(prevState => ({ ...prevState, [fieldName]: e }));
        } else {
            const { name, value } = e.target;
            setReview(prevState => ({ ...prevState, [name]: value }));
        }
    };


    useEffect(() => {
        fetchReviews();
    }, []);

    const fetchReviews = () => {
        // const userId = localStorage.getItem('userId')
        // const token = localStorage.getItem('token');
        // const config = {
        //     headers: {
        //         Authorization: `Bearer ${token}`
        //     }
        // }
        console.log(recipeId);
        authAxios
            .get(`http://localhost:8080/review/recipe/${recipeId}`)
            .then(response => {
                setReviews(response.data);
            })
            .catch(error => {
                console.error('Error fetching reviews', error);
            });
    }



    const handleSubmit = (e) => {
        const userId = localStorage.getItem('userId');
        // const token = localStorage.getItem('token');
        // const config = {
        //     headers: {
        //         Authorization: `Bearer ${token}`
        //     }
        // }

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
            .post(`http://localhost:8080/review/user/${userId}/recipe/${recipeId}`, review)
            .then(response => {
                // console.log(response.data.data.id);
                fetchReviews();
                navigate(`/recipes/${recipeId}`)
            })
            .catch(error => {
                console.error("Error submitting review", error);
            });
    }

    // const handleChange = (e) => {
    //     setReview({
    //         ...review,
    //         [e.target.name]: e.target.value
    //     });
    // }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <label>
                    Comment:
                    <input type='text' name="comment" value={review.comment} onChange={handleChange}/>
                </label>
                <label>
                    Rating:
                    <StarRating handleChange={handleChange} />
                </label>
                <input type="submit" value="Submit"/>
            </form>
            <h2>Reviews:</h2>
            {reviews.map(review =>(
                <div key={review.id}>
                    <p>Rating: {review.rating}</p>
                    <p>{review.comment}</p>
                <hr/>
                </div>
            ))}
        </div>
    );
}

export default ReviewPage;