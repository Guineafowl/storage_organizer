package olcia.products.controller;

import lombok.AllArgsConstructor;
import olcia.products.persistence.Photo;
import olcia.products.persistence.Room;
import olcia.products.repository.PhotoRepository;
import olcia.products.repository.RoomRepository;
import olcia.products.request.AddRoomRequest;
import olcia.products.request.AddStorageRequest;
import olcia.products.request.ChangeNameRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@AllArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;
    private final PhotoRepository photoRepository;

    @Transactional
    @PostMapping(path = "/room")
    public void addRoom(@RequestBody AddRoomRequest addRoomRequest) {
        roomRepository.save(Room.builder()
                .name(addRoomRequest.getName())
                .photo(photoRepository.findById(addRoomRequest.getPhotoId()).orElseThrow())
                .build());
    }

    @Transactional
    @DeleteMapping(path = "/room/{id}")
    public void deleteRoom(@PathVariable("id") long id) {
        roomRepository.deleteById(id);
    }

    @Transactional
    @PatchMapping(path = "/room/{id}/name")
    public void changeName(@PathVariable("id") long id, @RequestBody ChangeNameRequest changeNameRequest) {
        roomRepository.findById(id)
                .ifPresent(room -> room.changeName(changeNameRequest.getName()));
    }

    @Transactional
    @PostMapping(path = "/room/{id}/storage")
    public void addStorage(@PathVariable("id") long id, @RequestBody AddStorageRequest addStorageRequest) {
        roomRepository.findById(id)
                .ifPresent(room -> {
                    Photo photo = addStorageRequest.getPhotoId() == null ? null : photoRepository.findById(addStorageRequest.getPhotoId())
                            .orElse(null);
                    room.addStorage(addStorageRequest.getName(), photo);
                });
    }

    @Transactional(readOnly = true)
    @GetMapping(path = "/rooms")
    public List<Room> rooms() {
        return roomRepository.findAll();
    }

}
