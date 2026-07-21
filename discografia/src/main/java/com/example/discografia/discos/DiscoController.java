package com.example.discografia.discos;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.discografia.artistas.IArtistaRepository;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DiscoController {

	private final IDiscoRepository discoRepository;
	private final IArtistaRepository artistaRepository;

	public DiscoController(IDiscoRepository discoRepository, IArtistaRepository artistaRepository) {
		this.discoRepository = discoRepository;
		this.artistaRepository = artistaRepository;
	}

	@PostMapping(
		value = "/disco",
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> HandlePostDiscoRequest(@RequestBody Disco disco) {
		if (!artistaRepository.existsById(disco.idArtista)) {
			return new ResponseEntity<>("No se puede crear el disco porque el artista no existe", HttpStatus.NOT_FOUND);
		}

		try {
			Disco discoGuardado = discoRepository.save(disco);
			return new ResponseEntity<>(discoGuardado, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error al crear el disco", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(
		value = "/discos",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<List<Disco>> HandleGetDiscosRequest() {
		List<Disco> discos = discoRepository.findAll();
		return new ResponseEntity<>(discos, HttpStatus.OK);
	}

	@GetMapping(
		value = "/disco/{id}",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> HandleGetDiscoRequest(@PathVariable String id) {
		Optional<Disco> disco = discoRepository.findById(id);

		if (disco.isPresent()) {
			return new ResponseEntity<>(disco.get(), HttpStatus.OK);
		}

		return new ResponseEntity<>("Disco no encontrado", HttpStatus.NOT_FOUND);
	}

	@GetMapping(
		value = "/artista/{id}/discos",
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<List<Disco>> HandleGetDiscosByArtistaRequest(@PathVariable String id) {
		List<Disco> discos = discoRepository.findDiscosByIdArtista(id);
		return new ResponseEntity<>(discos, HttpStatus.OK);
	}
}
