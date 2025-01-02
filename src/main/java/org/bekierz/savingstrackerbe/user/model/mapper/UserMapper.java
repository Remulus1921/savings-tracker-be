package org.bekierz.savingstrackerbe.user.model.mapper;

import org.bekierz.savingstrackerbe.user.model.dto.UserDto;
import org.bekierz.savingstrackerbe.user.model.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDto toDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
