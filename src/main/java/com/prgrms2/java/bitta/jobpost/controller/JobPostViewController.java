package com.prgrms2.java.bitta.jobpost.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/job-post")
public class JobPostViewController {

    @GetMapping
    public String showJobpostPage(Model model) {
        return "jobpost/jobpost";
    }
}





//import edu.example.training.entity.Training;
//import edu.example.training.service.TrainingService;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Repository;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/training")
//public class TrainingController {
//    private final TrainingService trainingService;
//
//    public TrainingController(TrainingService trainingService) {
//        this.trainingService = trainingService;
//    }
//
//    @GetMapping("/display-list")
//    public String displayList(Model model){
//        System.out.println("/training/display-list");
//
//        List<Training> trainings = trainingService.findAll();
//        model.addAttribute("trainingList", trainings);
//
//        return "training/trainingList";
//    }
//
//    @GetMapping("/display-details")
//    public String displayDetails(@RequestParam String trainingId, Model model){
//        System.out.println("/training/display-details id : " + trainingId);
//
//        Training training = trainingService.findById(trainingId);
//        model.addAttribute("training", training);
//
//        return "training/trainingDetails";
//    }
//}




