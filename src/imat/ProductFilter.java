package imat;

import io.vavr.control.Either;
import org.jetbrains.annotations.Nullable;
import se.chalmers.cse.dat216.project.Product;
import se.chalmers.cse.dat216.project.ProductCategory;

import java.util.HashSet;
import java.util.List;

public class ProductFilter {
    @Nullable ProductFilter then;
    private Either<String, HashSet<ProductCategory>> filter;

    public ProductFilter(Either<String, List<ProductCategory>> filter) {
        this(filter, null);
    }
    ProductFilter(Either<String, List<ProductCategory>> filter, @Nullable ProductFilter then) {
        this.then = then;
        this.filter = filter.map(HashSet::new);
    }

    static final int bestPossibleMatch = 3;
    static int howGoodStringMatch(String search, String src){ //this can be improved
        src = src.toLowerCase();
        search = search.toLowerCase();
        if(src.equals(search)) return bestPossibleMatch;
        if(src.startsWith(search)) return 2;
        if(src.contains(search)) return 1;
        return -1;
    }
    int howGoodMatch(Product p){
        int matchScore;
        if(filter.isLeft()) {
            matchScore = howGoodStringMatch(filter.getLeft(), p.getName());
        } else {
            matchScore = filter.get().contains(p.getCategory()) ? 1 : -1;
        }
        if (then == null) return matchScore;
        int childMatch = then.howGoodMatch(p);
        return childMatch < 0 ? -1 : matchScore*(bestPossibleMatch+1)+childMatch;
    }
}