package com.codenest.ui;

import com.codenest.model.Post;
import javax.swing.*;
import java.awt.*;

public class ViewPostDialog extends JDialog {
    public ViewPostDialog(Frame parent, Post post) {
        super(parent, "View Post", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        add(new JLabel("Post: " + post.getTitle()), BorderLayout.CENTER);
    }
}
