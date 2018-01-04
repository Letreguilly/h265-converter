package fr.letreguilly.utils.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class ProccessUtils {


    public static Optional<String> execCommand(String command) {

        //setup
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CommandLine commandline = CommandLine.parse(command);
        DefaultExecutor exec = new DefaultExecutor();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        exec.setStreamHandler(streamHandler);

        //exec
        try {
            exec.execute(commandline);
            return Optional.of(outputStream.toString());
        } catch (IOException e) {
            log.error("an error occur when try to exec " + command, e);
            return Optional.empty();
        }
    }

    public static CompletableFuture<Optional<String>> execCommandAsync(String command){
        CompletableFuture<Optional<String>>  futureResult = new CompletableFuture();

        Optional<String> result = ProccessUtils.execCommand(command);

        futureResult.complete(result);

        return futureResult;
    }
}
