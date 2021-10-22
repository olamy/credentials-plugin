/*
 * The MIT License
 *
 * Copyright (c) 2011-2016, CloudBees, Inc., Stephen Connolly.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cloudbees.plugins.credentials.impl;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import hudson.Extension;
import hudson.Util;
import hudson.util.Secret;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Concrete implementation of {@link StandardUsernamePasswordCredentials}.
 *
 * @since 1.6
 */
@SuppressWarnings("unused") // read resolved by extension plugins
public class UsernamePasswordCredentialsImpl extends BaseStandardCredentials implements
        StandardUsernamePasswordCredentials {

    /**
     * The username.
     */
    @NonNull
    private final String username;

    /**
     * The password.
     */
    @NonNull
    private final Secret password;

    @Nullable
    private Boolean usernameSecret = false;

    /**
     * Constructor.
     *
     * @param scope       the credentials scope
     * @param id          the ID or {@code null} to generate a new one.
     * @param description the description.
     * @param username    the username.
     * @param password    the password.
     */
    @DataBoundConstructor
    @SuppressWarnings("unused") // by stapler
    public UsernamePasswordCredentialsImpl(@CheckForNull CredentialsScope scope,
                                           @CheckForNull String id, @CheckForNull String description,
                                           @CheckForNull String username, @CheckForNull String password) {
        super(scope, id, description);
        this.username = Util.fixNull(username);
        this.password = Secret.fromString(password);
    }

    private Object readResolve() {
        if (usernameSecret == null) {
            usernameSecret = true;
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    public Secret getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isUsernameSecret() {
        return usernameSecret;
    }

    @DataBoundSetter
    public void setUsernameSecret(boolean usernameSecret) {
        this.usernameSecret = usernameSecret;
    }

    /**
     * {@inheritDoc}
     */
    @Extension(ordinal = 1)
    @Symbol("usernamePassword")
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        /**
         * {@inheritDoc}
         */
        @NonNull
        @Override
        public String getDisplayName() {
            return Messages.UsernamePasswordCredentialsImpl_DisplayName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getIconClassName() {
            return "icon-credentials-userpass";
        }
    }
}
