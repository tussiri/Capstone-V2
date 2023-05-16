package org.launchcode.LiftoffRecipeProject.controllers;

import jakarta.validation.Valid;
import org.launchcode.LiftoffRecipeProject.data.ReviewRepository;
import org.launchcode.LiftoffRecipeProject.data.UserRepository;
import org.launchcode.LiftoffRecipeProject.models.Review;
import org.launchcode.LiftoffRecipeProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;


//    @PostMapping("add")
//    public String createReviews(@ModelAttribute Review newReview) {
//        reviewRepository.save(newReview);
//        return "redirect:";
//    }

    @PostMapping("/{id}")
    public ResponseEntity<Review> createReview(@PathVariable Integer userId, @Valid @RequestBody Review review) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            review.setUser(user);
            Review newReview = reviewRepository.save(review);
            return new ResponseEntity<>(newReview, HttpStatus.CREATED);
        }catch(Exception e){
            System.err.println("exception: "+ e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviws() {
        List<Review> reviews = (List<Review>) reviewRepository.findAll();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable int id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            return new ResponseEntity<>(review.get(), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Review> deleteReview(@PathVariable int id){
        Optional<Review> selectedReview = reviewRepository.findById(id);
        if(selectedReview.isPresent()){
            reviewRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable int id, @RequestBody Review review){
        Optional<Review> selectedReview = reviewRepository.findById(id);
        if(selectedReview.isPresent()){
            Review updatedReview = selectedReview.get();
            updatedReview.setRating(review.getRating());
            updatedReview.setComment(review.getComment());
            updatedReview.setRecipe(review.getRecipe());
            reviewRepository.save(updatedReview);
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




}