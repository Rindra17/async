package hei.school.async.concurrency;

import static hei.school.async.concurrency.ThreadRenamer.getRandomSubThreadNamePrefixFrom;
import static hei.school.async.concurrency.ThreadRenamer.renameThread;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor;

import hei.school.async.PojaGenerated;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@PojaGenerated
@Component
public class Workers {
  private final ExecutorService executorService;

  public Workers() {
    this.executorService = newVirtualThreadPerTaskExecutor();
  }

  @SneakyThrows
  public List<Void> invokeAll(List<Callable<Void>> callables) {
    var parentThread = currentThread();
    callables =
        callables.stream()
            .map(
                c ->
                    (Callable<Void>)
                        () -> {
                          renameThread(
                              parentThread, getRandomSubThreadNamePrefixFrom(parentThread));
                          return c.call();
                        })
            .toList();
    List<Future<Void>> futures = executorService.invokeAll(callables);
    return futures.stream().map(this::handleFutureException).toList();
  }

  private Void handleFutureException(Future<Void> future) {
    try {
      return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}
