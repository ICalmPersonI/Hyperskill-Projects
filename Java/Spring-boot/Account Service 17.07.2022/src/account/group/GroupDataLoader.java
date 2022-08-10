package account.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class GroupDataLoader {
    private GroupRepository repository;

    @Autowired
    GroupDataLoader(GroupRepository repository) {
        this.repository = repository;
        fillingInTable();
    }

    private void fillingInTable() {
        Stream.concat(Group.ADMIN_GROUP.stream(), Group.CUSTOMER_GROUP.stream()).forEach(group -> {
            if (repository.findByCode(group.getCode()).isEmpty()) {
                repository.save(group);
            }
        });
    }
}