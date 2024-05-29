package br.com.oceanex.model;

import br.com.oceanex.validation.TipoStatusFeedback;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedBackImagem {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @TipoStatusFeedback
    @NotBlank(message = "{feedbackimagem.statusfeedback.notblank}")
    @Size(min = 3, max = 100, message="{feedbackimagem.statusfeedback.size}")
    private String statusFeedback; // Like ou Deslike

    @Size(min = 3, message="{feedbackimagem.descricaofeedback.size}")
    private String descricaoFeedback; // O usuário poderá enviar uma mensagem em seu feedback dizendo porque gostou ou não do resultado da predição.

}
