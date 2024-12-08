package se.citerus.dddsample.application.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import se.citerus.dddsample.application.command.HandlingReportProcessCommand;
import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;
import se.citerus.dddsample.application.configure.CargoTrackerApplicationConfigure;
import se.citerus.dddsample.application.service.HandlingReportProcessor;
import se.citerus.dddsample.application.service.HandlingReportHandler;
import se.citerus.dddsample.domain.model.handling.HandlingReport;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@SpringBootTest(
    classes = {
        CargoTrackerApplicationConfigure.class,
        HandlingReportProcessor.class
    },
    properties = {
        "cargotracker.application.handing-report.process-strategy=THREAD",
    })
class ThreadPooledHandlingReportHandlerTest {

    @TestBean
    TaskExecutor taskExecutor;

    @MockitoBean
    HandlingReportProcessor processor;

    @Autowired
    HandlingReportHandler handlingReportHandler;

    private static TaskExecutor taskExecutor() {
        return new SyncTaskExecutor();
    }

    @Test
    void receiveHandlingReport() {
        var report1 = Mockito.mock(HandlingReport.class);
        var report2 = Mockito.mock(HandlingReport.class);
        var command = HandlingReportReceiveCommand.builder()
            .reports(List.of(report1, report2))
            .build();

        handlingReportHandler.receiveHandlingReport(command);

        then(processor).should(times(2)).processHandingEvent(Mockito.any(HandlingReportProcessCommand.class));
    }


    @Test
    void receiveHandlingReport_with_process_error() {
        var report1 = Mockito.mock(HandlingReport.class);
        var report2 = Mockito.mock(HandlingReport.class);
        var command = HandlingReportReceiveCommand.builder()
            .reports(List.of(report1, report2))
            .build();

        doThrow(IllegalStateException.class).when(processor).processHandingEvent(any(HandlingReportProcessCommand.class));
        handlingReportHandler.receiveHandlingReport(command);

        then(processor).should(times(2)).processHandingEvent(Mockito.any(HandlingReportProcessCommand.class));
    }
}