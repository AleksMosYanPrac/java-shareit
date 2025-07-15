package ru.practicum.shareit.item;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "items", schema = "public")
public class Item {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotNull
   @Column(name = "owner_id")
   private Long ownerId;

   @NotBlank
   private String name;

   @NotBlank
   private String description;

   @NotNull
   private Boolean available;

    public boolean isAvailable() {
       return available;
    }
}