package se.citerus.dddsample.application.service;

import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;

/**
 * This class can not guarantee the command be processed immediately
 * if you want the event be processed immediately please refer to {@link HandlingEventProcessor}
 */
public interface HandlingEventReceiver {

    void receiveHandlingReport(HandlingReportReceiveCommand command);

}
