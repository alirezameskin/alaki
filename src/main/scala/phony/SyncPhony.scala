package phony

import cats.effect.Sync

import scala.language.higherKinds

/**
 * A default instance for any `F[_]` with a `Sync` instance.
 */
private class SyncPhony[F[_]](implicit F: Sync[F], L: Locale[F]) extends Phony[F]