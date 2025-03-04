package edu.guet.studentworkmanagementsystem.common;

import edu.guet.studentworkmanagementsystem.entity.dto.user.RegisterUser;
import lombok.Getter;

import java.util.List;
import java.util.Set;

public class UsernameTrie {
    private final TrieNode root;
    public UsernameTrie() {
        root = new TrieNode();
    }

    public void insert(String username, Set<String> roles) {
        TrieNode node = root;
        for (char ch: username.toCharArray()) {
            int idx = ch - '0';
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

    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
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

    public void buildTrie(List<RegisterUser> registerUsers) {
        registerUsers.forEach(registerUser -> {
            String username = registerUser.getUsername();
            Set<String> roles = registerUser.getRoles();
            insert(username, roles);
        });
    }
    @Getter
    public static class TrieNode {
        TrieNode[] children = new TrieNode[10];
        Set<String> roles;
        boolean isEnd = false;
    }
}
