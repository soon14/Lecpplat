/**
 * SrpUserDetails.java	  V1.0   2013-2-27 上午9:31:21
 *
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.zengshi.ecp.base.security;

import com.zengshi.ecp.server.front.security.AuthPrivilegeResDTO;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;


@JsonAutoDetect
@JsonIgnoreProperties({"password","username","authorities","accountNonExpired","accountNonLocked","credentialsNonExpired","enabled"})
public class EcpUserDetails implements UserDetails {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -772003705075915650L;
    private String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private AuthPrivilegeResDTO authPrivilege;
    private Map<String,Object> affiliatedObject;
    
    public EcpUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, true, true, true, true, authorities);
    }
    public EcpUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {

        if (((username == null) || "".equals(username))) {// || (password == null)
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }

        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {

        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {

        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {

        return this.enabled;
    }
    
    /**
     * 
     * 功能描述：获取附带对象
     *
     * 创建日期 ：2013-3-5 下午2:15:51
     *
     * @return
     *
     * 修改历史 ：(修改人，修改时间，修改原因/内容)
     */
    public Map<String,Object> getAffiliatedObject() {
    
        return affiliatedObject;
    }
    
    
    public AuthPrivilegeResDTO getAuthPrivilege() {
        return authPrivilege;
    }
    public void setAuthPrivilege(AuthPrivilegeResDTO authPrivilege) {
        this.authPrivilege = authPrivilege;
    }
    /**
     * 
     * 功能描述：设置附带对象
     *
     * 创建日期 ：2013-3-5 下午2:16:24
     *
     * @param affiliatedObject
     *
     * 修改历史 ：(修改人，修改时间，修改原因/内容)
     */
    public void setAffiliatedObject(Map<String,Object> affiliatedObject) {
    
        this.affiliatedObject = affiliatedObject;
    }
    public void eraseCredentials() {
        this.password=null;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities =
            new TreeSet<GrantedAuthority>(new AuthorityComparator());

        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to the set.
            // If the authority is null, it is a custom authority and should precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }

    /**
     * Returns {@code true} if the supplied object is a {@code User} instance with the
     * same {@code username} value.
     * <p>
     * In other words, the objects are equal if they have the same username, representing the
     * same principal.
     */
    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof EcpUserDetails) {
            return username.equals(((EcpUserDetails) rhs).username);
        }
        return false;
    }

    /**
     * Returns the hashcode of the {@code username}.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");
        sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
        sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
        sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

        if (!authorities.isEmpty()) {
            sb.append("Granted Authorities: ");

            boolean first = true;
            for (GrantedAuthority auth : authorities) {
                if (!first) {
                    sb.append(",");
                }
                first = false;

                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }
}
