package com.example.discografia.artistas;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ArtistaController {

	private final IArtistaRepository artistaRepository;

	public ArtistaController(IArtistaRepository artistaRepository) {
		this.artistaRepository = artistaRepository;
	}

	@PostMapping(
		value = "/artista",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> HandleInsertArtistaRequest(@RequestBody Artista artista) {
		try {
			Artista artistaGuardado = artistaRepository.save(artista);
			return new ResponseEntity<>(artistaGuardado, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al crear el artista", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(
		value = "/artistas",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<List<Artista>> HandleGetAristasRequest() {
		List<Artista> artistas = artistaRepository.findAll();
		return new ResponseEntity<>(artistas, HttpStatus.OK);
	}

	@GetMapping(
		value = "/artista/{id}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> HandleGetArtistaRequest(@PathVariable String id) {
		Optional<Artista> artista = artistaRepository.findById(id);

		if (artista.isPresent()) {
			return new ResponseEntity<>(artista.get(), HttpStatus.OK);
		}

		return new ResponseEntity<>("Artista no encontrado", HttpStatus.NOT_FOUND);
	}

	@PutMapping(
		value = "/artista/{id}",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> HandleUpdateArtistaRequest(
		@PathVariable String id,
		@RequestBody Artista artista
	) {
		if (!artistaRepository.existsById(id)) {
			return new ResponseEntity<>("Artista no encontrado", HttpStatus.NOT_FOUND);
		}

		try {
			artista._id = id;
			Artista artistaActualizado = artistaRepository.save(artista);
			return new ResponseEntity<>(artistaActualizado, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al actualizar el artista", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(
		value = "/artista/{id}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> HandleDeleteArtistaRequest(@PathVariable String id) {
		if (!artistaRepository.existsById(id)) {
			return new ResponseEntity<>("Artista no encontrado", HttpStatus.NOT_FOUND);
		}

		try {
			artistaRepository.deleteById(id);
			return new ResponseEntity<>("Artista eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al eliminar el artista", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
