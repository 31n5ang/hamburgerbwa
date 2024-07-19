package kr.hamburgersee.domain.review;

import jakarta.validation.Valid;
import kr.hamburgersee.domain.file.image.ReviewImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;

    private static final String REVIEW_CREATE_FORM = "review-create";
    private static final String REVIEW = "review";

    @ResponseBody
    @PostMapping("/image")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        return reviewImageService.uploadReviewImages(file);
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new ReviewCreateForm());
        return REVIEW_CREATE_FORM;
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("form") ReviewCreateForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return REVIEW_CREATE_FORM;
        }

        Long reviewId = reviewService.writeProcess(form, null);

        return "redirect:/review/" + reviewId;
    }

    @GetMapping("/{id}")
    public String review(
            Model model,
            @PathVariable("id") Long reviewId
    ) {
        ReviewDto reviewDto = reviewService.getReviewDto(reviewId);
        model.addAttribute("review", reviewDto);
        return REVIEW;
    }
}

