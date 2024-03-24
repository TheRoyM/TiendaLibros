package TheRoy.tienda_libros.repositorio;

import TheRoy.tienda_libros.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro,Integer> {

}
