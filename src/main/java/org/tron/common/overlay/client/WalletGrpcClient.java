package org.tron.common.overlay.client;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.tron.api.GrpcAPI.*;
import org.tron.api.WalletGrpc;
import org.tron.protos.Contract;
import org.tron.protos.Contract.AssetIssueContract;
import org.tron.protos.Protocol.Account;
import org.tron.protos.Protocol.Block;
import org.tron.protos.Protocol.Transaction;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class WalletGrpcClient {

  private final ManagedChannel channel;
  private final WalletGrpc.WalletBlockingStub walletBlockingStub;

  public WalletGrpcClient(String host, int port) {
    channel = ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext(true)
        .build();
    walletBlockingStub = WalletGrpc.newBlockingStub(channel);
  }

  public WalletGrpcClient(String host) {
    channel = ManagedChannelBuilder.forTarget(host)
        .usePlaintext(true)
        .build();
    walletBlockingStub = WalletGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public Account queryAccount(byte[] address) {
    ByteString addressBS = ByteString.copyFrom(address);
    Account request = Account.newBuilder().setAddress(addressBS).build();
    return walletBlockingStub.getAccount(request);
  }

  public Transaction createTransaction(Contract.TransferContract contract) {
    return walletBlockingStub.createTransaction(contract);
  }

  public Transaction createTransferAssetTransaction(Contract.TransferAssetContract contract) {
    return walletBlockingStub.transferAsset(contract);
  }

  public Transaction createParticipateAssetIssueTransaction(
      Contract.ParticipateAssetIssueContract contract) {
    return walletBlockingStub.participateAssetIssue(contract);
  }

  public Transaction createAccount(Contract.AccountCreateContract contract) {
    return walletBlockingStub.createAccount(contract);
  }

  public Transaction createAssetIssue(AssetIssueContract contract) {
    return walletBlockingStub.createAssetIssue(contract);
  }

  public Transaction voteWitnessAccount(Contract.VoteWitnessContract contract) {
    return walletBlockingStub.voteWitnessAccount(contract);
  }

  public Transaction createWitness(Contract.WitnessCreateContract contract) {
    return walletBlockingStub.createWitness(contract);
  }

  public boolean broadcastTransaction(Transaction signaturedTransaction) {
    Return response = walletBlockingStub.broadcastTransaction(signaturedTransaction);
    return response.getResult();
  }

  public Block getBlock(long blockNum) {
    if (blockNum < 0) {
      return walletBlockingStub.getNowBlock(EmptyMessage.newBuilder().build());
    }
    NumberMessage.Builder builder = NumberMessage.newBuilder();
    builder.setNum(blockNum);
    return walletBlockingStub.getBlockByNum(builder.build());
  }

  public Optional<AccountList> listAccounts() {
    AccountList accountList = walletBlockingStub.listAccounts(EmptyMessage.newBuilder().build());
    if (accountList != null) {
      return Optional.of(accountList);
    }
    return Optional.empty();
  }

  public Optional<WitnessList> listWitnesses() {
    WitnessList witnessList = walletBlockingStub.listWitnesses(EmptyMessage.newBuilder().build());
    if (witnessList != null) {
      return Optional.of(witnessList);
    }
    return Optional.empty();
  }

  public Optional<AssetIssueList> getAssetIssueList() {
    AssetIssueList assetIssueList = walletBlockingStub
        .getAssetIssueList(EmptyMessage.newBuilder().build());
    if (assetIssueList != null) {
      return Optional.of(assetIssueList);
    }
    return Optional.empty();
  }

  public Optional<NodeList> listNodes() {
    NodeList nodeList = walletBlockingStub
        .listNodes(EmptyMessage.newBuilder().build());
    if (nodeList != null) {
      return Optional.of(nodeList);
    }
    return Optional.empty();
  }

  public Optional<AssetIssueList> getAssetIssueByAccount(byte[] address) {
    ByteString addressBS = ByteString.copyFrom(address);
    Account request = Account.newBuilder().setAddress(addressBS).build();
    AssetIssueList assetIssueList = walletBlockingStub
        .getAssetIssueByAccount(request);
    if (assetIssueList != null) {
      return Optional.of(assetIssueList);
    }
    return Optional.empty();
  }

  public AssetIssueContract getAssetIssueByName(String assetName) {
    ByteString assetNameBs = ByteString.copyFrom(assetName.getBytes());
    BytesMessage request = BytesMessage.newBuilder().setValue(assetNameBs).build();
    return walletBlockingStub.getAssetIssueByName(request);
  }

  public NumberMessage getTotalTransaction() {
    return walletBlockingStub.totalTransaction(EmptyMessage.newBuilder().build());
  }

}
