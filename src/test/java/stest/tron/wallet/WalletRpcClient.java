package stest.tron.wallet;

import lombok.extern.slf4j.Slf4j;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tron.api.WalletGrpc;

@Slf4j
public class WalletRpcClient {
    private final ManagedChannel channel;
    private final WalletGrpc.WalletBlockingStub blockingStub;

    public WalletRpcClient(String host, int port) {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true);
        channel = channelBuilder.build();
        blockingStub = WalletGrpc.newBlockingStub(channel);
    }


    public void shutdown() throws Exception {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        WalletRpcClient client = new WalletRpcClient("127.0.0.1", 50011);


        for (int i = 0; i < 5; i++) {
        }

    }

}
