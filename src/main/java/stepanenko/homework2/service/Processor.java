package stepanenko.homework2.service;

import stepanenko.practice1.model.Message;

public interface Processor {
    void process(Message message);
    Message processMessage(Message message);
}
