package org.burgas.portfolioservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "project", schema = "public")
@NamedEntityGraph(
        name = "project-with-portfolio",
        attributeNodes = {
                @NamedAttributeNode(value = "portfolio"),
                @NamedAttributeNode(value = "images"),
                @NamedAttributeNode(value = "videos"),
                @NamedAttributeNode(value = "documents"),
        }
)
public final class Project extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "portfolio_id", referencedColumnName = "id")
    private Portfolio portfolio;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Image.class)
    @JoinTable(
            name = "project_image",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id")
    )
    private List<Image> images;

    public void addImage(final Image image) {
        this.images.add(image);
    }

    public void removeImage(final Image image) {
        this.images.remove(image);
    }

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Video.class)
    @JoinTable(
            name = "project_video",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "video_id", referencedColumnName = "id")
    )
    private List<Video> videos;

    public void addVideo(final Video video) {
        this.videos.add(video);
    }

    public void removeVideo(final Video video) {
        this.videos.remove(video);
    }

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Document.class)
    @JoinTable(
            name = "project_document",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id")
    )
    private List<Document> documents;

    public void addDocument(final Document document) {
        this.documents.add(document);
    }

    public void removeDocument(final Document document) {
        this.documents.remove(document);
    }
}
