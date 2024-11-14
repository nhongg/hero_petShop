package com.example.heroPetShop.Presenter;

import com.example.heroPetShop.Models.Story;
import com.example.heroPetShop.my_interface.IStory;
import com.example.heroPetShop.my_interface.StoryView;

public class StoryPresenter implements IStory {

    private Story story;
    private StoryView callback;

    public StoryPresenter(StoryView callback){
        story = new Story(this);
        this.callback = callback;
    }

    public void HandleGetStory(String iduser){
        story.HandleGetStory(iduser);
    }
    @Override
    public void getDataStory(String noidung) {
        callback.getDataStory(noidung);
    }
}
