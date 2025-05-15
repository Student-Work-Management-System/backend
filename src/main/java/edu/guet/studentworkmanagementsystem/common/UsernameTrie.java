package edu.guet.studentworkmanagementsystem.common;

import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUserRequest;
import edu.guet.studentworkmanagementsystem.exception.ServiceException;
import edu.guet.studentworkmanagementsystem.exception.ServiceExceptionEnum;
import lombok.Getter;

import java.util.List;
import java.util.Set;

public class UsernameTrie {
    private final TrieNode root;
    public UsernameTrie() {
        root = new TrieNode();
    }

    private int getIndex(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0'; // 0 - 9
        } else if (ch >= 'a' && ch <= 'z') {
            return ch - 'a' + 10; // 10 - 35
        } else if (ch >= 'A' && ch <= 'Z') {
            return ch - 'A' + 36; // 36 - 61
        } else {
            throw new ServiceException(ServiceExceptionEnum.TYPE_ERROR.getCode(), ServiceExceptionEnum.TYPE_ERROR.getMsg() + ", 不支持此字符作为用户名: " + ch);
        }
    }

    public void insert(String username, Set<String> roles) {
        TrieNode node = root;
        for (char ch: username.toCharArray()) {
            int idx = getIndex(ch);
            if (node.children[idx] == null) {
                node.children[idx] = new TrieNode();
            }
            node = node.children[idx];
        }
        node.isEnd = true;
        node.roles = roles;
    }

    public TrieNode getNode(String username) {
        return searchPrefix(username);
    }

    public boolean search(String username) {
        TrieNode node = searchPrefix(username);
        return node != null && node.isEnd;
    }

    private TrieNode searchPrefix(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toCharArray()) {
            int index = ch - '0';
            if (node.children[index] == null) {
                return null;
            }
            node = node.children[index];
        }
        return node;
    }

    public void buildTrie(List<RegisterUserRequest> registerUserRequests) {
        registerUserRequests.forEach(registerUser -> {
            String username = registerUser.getUsername();
            Set<String> roles = registerUser.getRoles();
            insert(username, roles);
        });
    }
    @Getter
    public static class TrieNode {
        TrieNode[] children = new TrieNode[62];
        Set<String> roles;
        boolean isEnd = false;
    }
}
