package pl.matkan.wholesaler.auth;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
public class AccessControlService {

    public void canAccessResource(Long resourceOwnerId, UserDetailsImpl userDetails) {

        boolean isAdminOrModerator = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN") ||
                                grantedAuthority.getAuthority().equals("ROLE_MODERATOR"));

        boolean isOwner = resourceOwnerId.equals(userDetails.getId());

        if (!isAdminOrModerator && !isOwner) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }


    }

}
