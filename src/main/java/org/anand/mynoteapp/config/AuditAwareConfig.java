package org.anand.mynoteapp.config;

import org.anand.mynoteapp.entity.User;
import org.anand.mynoteapp.utils.CommonUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareConfig implements AuditorAware<Integer> {
    @Override
    public Optional<Integer> getCurrentAuditor() {
        User loggedInUser = CommonUtil.getLoggedInUser();
        return Optional.of(loggedInUser.getUserId());
    }
}
